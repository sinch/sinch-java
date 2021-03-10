package com.sinch.openapi

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskContainer
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

import javax.inject.Inject

abstract class OpenApiExtension {
    private final Project project
    private final TaskContainer tasks
    private final SourceSetContainer sourceSets

    @Inject
    OpenApiExtension(Project project, TaskContainer tasks, SourceSetContainer sourceSets) {
        this.project = project
        this.tasks = tasks
        this.sourceSets = sourceSets
    }

    void modelDefinition(@DelegatesTo(OpenApiGeneratorDsl) final Closure closure) {
        OpenApiGeneratorDsl openApiGeneratorDsl = new OpenApiGeneratorDsl()
        openApiGeneratorDsl.with closure
        def apiName = openApiGeneratorDsl.apiName

        def swaggerFile = new File("$project.buildDir/${apiName}.swagger.json")
        def downloadSwaggerFileTask = tasks.create("get${apiName}Swagger", AntDownload) {
            sourceUrl = openApiGeneratorDsl.url
            target = swaggerFile
        }
        def generateClassesTask = tasks.create("openApiGenerate${apiName}", GenerateTask) {
            generatorName = "java"
            library = "native"
            inputSpec = swaggerFile.toString()
            outputDir = "$project.buildDir/swagger"
            modelPackage = "com.sinch.sdk.model.${apiName}"
            generateModelTests = false
            generateModelDocumentation = false
            validateSpec = false
            modelFilesConstrainedTo = [""]
            configOptions = ['hideGenerationTimestamp': 'true']
        }
        generateClassesTask.dependsOn downloadSwaggerFileTask
        if (openApiGeneratorDsl.prefixesToRemove) {
            def args = ["./remove_model_prefix.sh", apiName]
            args.addAll(openApiGeneratorDsl.prefixesToRemove)
            def renameSwaggerModels = tasks.create("rename${apiName}SwaggerModels", Exec) {
                description = "Removes prefixes from the swagger generated models."
                commandLine args
            }
            generateClassesTask.finalizedBy(renameSwaggerModels)
        }
        sourceSets.main.java.srcDir "$project.buildDir/swagger/src/main/java"
        tasks.findAll {it.name.startsWith('compileJava')}.each {it.dependsOn generateClassesTask}
    }

    static class AntDownload extends DefaultTask {
        @Input
        String sourceUrl

        @OutputFile
        File target

        @TaskAction
        void download() {
            println("$sourceUrl -> $target")
            ant.get(src: sourceUrl, dest: target)
        }
    }

    static class OpenApiGeneratorDsl {
        String apiName
        String url
        String[] prefixesToRemove

        def apiName(String apiName) {
            this.apiName = apiName
        }

        def url(String url) {
            this.url = url
        }

        def prefixesToRemove(String... prefixes) {
            this.prefixesToRemove = prefixes
        }
    }
}

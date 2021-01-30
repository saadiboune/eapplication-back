package com.eapplication.eapplicationback;

import com.eapplication.eapplicationback.models.bdd.AutoComplModel;
import com.eapplication.eapplicationback.services.AutoComplService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@SpringBootApplication
@EnableCaching
public class EapplicationBackApplication implements CommandLineRunner  {

    @Autowired
    private AutoComplService autoComplService;

    @Autowired
    DataSource datasource;

    public static void main(String[] args) {
        SpringApplication.run(EapplicationBackApplication.class, args);
    }

    @Override
    public void run(String... args) {

        // init data from sql
        this.initUsingSql();

        //this.initUsingFiles();

    }

    /**
     * Init de la table qui sert à l'autocompletion via un fichier sql
     */
    private void initUsingSql(){
        if(autoComplService.count() == 0){
            System.out.println("Début de l'init via fichier sql ");
            ResourceDatabasePopulator rdp = new ResourceDatabasePopulator();
            rdp.addScript(new ClassPathResource("init/init-nodes.sql"));
            rdp.execute(datasource);

            System.out.println("Nombre de données insérées via le fichier sql : " + autoComplService.count());
        }
    }

    /**
     * Lit de dossier ./src/main/resources/files/ parse en {@link AutoComplModel} et save dans la table de l'autoCompl
     *
     */
    private void initUsingFiles(){
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        List<File> files = filesToInsert();
        // clean de la table
        autoComplService.deleteAll();

        for (File file : files) {
            try {
                AutoComplModel[] autoComplModels = objectMapper.readValue(file, AutoComplModel[].class);
                autoComplService.saveAll(Arrays.asList(autoComplModels));
            } catch (IOException e) {
                System.out.println("Erreur lors du parsing du ficher " + file.getName() + e.getMessage());
            }

        }

        System.out.println("Nombre de données insérées via les fichiers json : " + autoComplService.count());
    }

    /**
     * Récupération de la liste des fichier qui servent à l'init de la table {@link AutoComplModel}
     *
     * Cette table est utilisée pour les recherche avec autocompl
     *
     * @return
     */
    private List<File> filesToInsert() {
        List<File> initFiles = new ArrayList<>();
        // Récupération du dossier qui contient la liste des fiichiers pour l'init
        File initFolder = Paths.get("./src/main/resources/files/").toFile();

        if(initFolder.isDirectory()){
            initFiles.addAll(filesOfDir(initFolder));
        }
        return initFiles;
    }

    /**
     *  Récupère la liste des fichier dans un dossier
     * @param dir
     * @return
     */
    private static Set<File> filesOfDir(File dir) {
        if(dir != null && dir.isDirectory()){
            return Stream.of(Objects.requireNonNull(dir.listFiles()))
                    .filter(file -> !file.isDirectory())
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }
}

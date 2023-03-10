package fr.iwaki.cosmos;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

    public void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);

        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());

                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();

                zis.closeEntry();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void downloadFromInternet(String fileUrl, File dest) throws IOException {
        URL url = new URL(fileUrl);
        InputStream is = url.openStream();
        OutputStream os = Files.newOutputStream(dest.toPath());

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    public void deleteDirectory(File directory) throws NullPointerException {
        for (File file: Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                deleteDirectory(file);
                System.out.println("Deleted '" + file + "' directory");
            } else {
                file.delete();
                System.out.println("Deleted '" + file + "' file");
            }
        }
    }

    public void copyFile(File src, File dest) throws IOException {
        // Cr??er l'objet File Reader
        FileReader fr = new FileReader(src);
        // Cr??er l'objet BufferedReader
        BufferedReader br = new BufferedReader(fr);
        // Cr??er l'objet File Writer
        FileWriter fw = new FileWriter(dest);
        String str;
        // Copie le contenu dans le nouveau fichier
        while((str = br.readLine()) != null)
        {
            fw.write(str);
            fw.write(System.lineSeparator());
            fw.flush();
        }
        fw.close();
    }

    public void copyFiles(File src, File dest) throws IOException {
        if(src.isDirectory()){
            //si le r??pertoire n'existe pas, cr??ez-le
            if(!dest.exists()){
                dest.mkdir();
                System.out.println("Dossier "+ src + "  > " + dest);
            }
            //lister le contenu du r??pertoire
            String[] files = src.list();

            for (String f : files) {
                //construire la structure des fichiers src et dest
                File srcF = new File(src, f);
                File destF = new File(dest, f);
                //copie r??cursive
                copyFiles(srcF, destF);
            }
        }else{
            //si src est un fichier, copiez-le.
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];
            int length;
            //copier le contenu du fichier
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
            System.out.println("Fichier " + src + " > " + dest);
        }
    }
}
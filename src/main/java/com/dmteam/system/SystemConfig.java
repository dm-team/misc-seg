package com.dmteam.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2014/12/8.
 */
public class SystemConfig {

    public final static Charset SYSTEM_CHARSET = Charset.forName("utf-8");

    private final static String WINDOWS = "windows";

    private final static String LINUX = "linux";

    public static String NLPIRModuleName() {

        String osName = osName();
        byte osBits;

        try {
            osBits = osBits();
        } catch (IOException e) {
            return null;
        }

        StringBuilder s = new StringBuilder();

        if (WINDOWS == osName) {
            s.append("NLPIR");
        } else if (LINUX == osName) {
            s.append("libNLPIR");
        }

        if ((byte)64 == osBits) {
            s.append("64");
        }
        return s.toString();
    }

    /**
     *
     * @return "linux" / "windows" / ""
     */
    public synchronized static String osName() {

        String osName = System.getProperty("os.name");

        osName = osName.toLowerCase();
        if (osName.contains(LINUX)) return LINUX;
        if (osName.contains(WINDOWS)) return WINDOWS;
        return "";
    }

    /**
     * 当前操作系统位数 返回 "32" / "64"
     */
    public synchronized static byte osBits() throws IOException {

        String osType = osName();
        byte defaultB = 32;

        if(WINDOWS == osType){
            String arch = System.getenv("PROCESSOR_ARCHITECTURE");
            String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
            if(arch.endsWith("64")|| wow64Arch != null && wow64Arch.endsWith("64")){
                return 64;
            }else{
                return defaultB;
            }
        }


        if (LINUX == osType) {
            BufferedReader bufferedReader = null;
            try {
                Process process = Runtime.getRuntime().exec("getconf LONG_BIT");
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s=bufferedReader.readLine();
                if(s.contains("64")){
                    return 64;
                }
                return defaultB;

            } finally {
                if (null != bufferedReader)
                    try { bufferedReader.close(); } catch (IOException e){}
            }
        }

        return defaultB;
    }
}

package com.uws.jupiter.common.apollo;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

/**
 * Apollo é a engine para rodar códigos Java em tempo de execução, e que dá toda a confiança que sua empresa precisa!
 */
public class Apollo implements Runnable{
    /**
     * @param sourceCode
     */
    public static CodeResult runCode(String sourceCode, String [] args){
        // Limpa a classe para ser roda 
        // sourceCode = tidySourceCode(sourceCode);

        // Pega o nome da classe principal 
        String qualifiedClassName = getClassName(sourceCode);

        // Fazendo o processo de transformação String -> Código Fonte e compilando  
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        InMemoryFileManager manager = new InMemoryFileManager(compiler.getStandardFileManager(null, null, null));
        List<JavaFileObject> sourceFiles = Collections.singletonList(new JavaSourceFromString(qualifiedClassName, sourceCode));
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, sourceFiles);

        if (!task.call()) {
            // Se teve problema de compilação, retorna eles.
            StringBuilder sb = new StringBuilder();
            diagnostics.getDiagnostics().forEach(d -> sb.append(String.valueOf(d)));

            return CodeResult.parse(null, null, sb.toString(), true);
            
        } else {
            try{
                // Criar uma instância da classe 
                ClassLoader classLoader = manager.getClassLoader(null);
                Class<?> clazz = classLoader.loadClass(qualifiedClassName);
                Object objectInstance = clazz.getDeclaredConstructor().newInstance();

                // Arranca a main da classe
                if(args == null)
                    args = new String[0];
                Method mainMethod = clazz.getDeclaredMethod("main", String[].class);
                
                // Prepara pra redirecionar os logs pra uma string
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(outputStream);
                // Salva o printstream antigo e coloca o novo
                PrintStream old = System.out;
                System.setOut(ps);

                // Invoca a main e roda
                Object output = mainMethod.invoke(objectInstance, (Object) args);
                
                // Volta para o antigo PrintStream
                System.out.flush();
                System.setOut(old);

                return CodeResult.parse(output, outputStream.toString(), null, false);
            }
            catch(Exception e){
                // TODO: Retornar algo aqui 
                System.out.println(e.getMessage());
                return CodeResult.parse(null, null, e.getMessage(), true);
            }
        }
    }

    private static String getClassName (String sourceCode){
        String [] parts = sourceCode.split("\n");
        for (String st : parts){
            if (st.contains("class")){
                String [] tokens = st.split(" ");

                int classnameIndex = 0;
                for(int i = 0; i < tokens.length; i++){
                    if(tokens[i].equals("class")){
                        classnameIndex = i + 1;   
                    }
                }
                return tokens[classnameIndex];
            }
        }

        return null;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }   
}
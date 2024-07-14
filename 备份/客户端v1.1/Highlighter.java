import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JTextPane;
import javax.swing.text.*;
//定义 Highlighter类,用于文本高亮的显示
public class Highlighter {

    //根据传入的语言类型调用相应的高亮方法
    public static void applyHighlight(JTextPane textPane, String language) {

        //获取文本组件（如字体、颜色、粗体、斜体等）的 StyledDocument 对象
        StyledDocument doc = textPane.getStyledDocument(); 
        //获取纯文本内容
        String text = textPane.getText();

        // 将整个文本范围设置为默认的样式
        doc.setCharacterAttributes(0, text.length(), textPane.getStyle("default"), true);

        //根据不同的语言选择不同的高亮
        if (language.equals("Java")) {
            highlightJava(textPane, text);
        } else if (language.equals("Python")) {
            highlightPython(textPane, text);
        } else if (language.equals("C++")) {
            highlightCpp(textPane, text);
        }
    }

/* 
    //Java 关键字的高亮
    private static void highlightJava(JTextPane textPane, String text) {

        //获取文本组件的 StyledDocument 对象
        StyledDocument doc = textPane.getStyledDocument(); 
        //在textPane中添加一个新的样式（基于默认样式来创建），并将其命名为"Keyword"
        Style keywordStyle = textPane.addStyle("Keyword", null); 
        //设置关键字的高亮色为蓝色
        StyleConstants.setForeground(keywordStyle, Color.BLUE);

        //Java 关键字的列表
        String[] keywords = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", 
                             "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", 
                             "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", 
                             "interface", "long", "native", "new", "package", "private", "protected", "public", 
                             "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", 
                             "throw", "throws", "transient", "try", "void", "volatile", "while"};

        //遍历文本，对每个关键字进行高亮显示
        for (String keyword : keywords) {
            int lastIndex = 0;
            //循环查找Java关键字在文本中的位置
            while ((lastIndex = text.indexOf(keyword, lastIndex)) != -1) {
                //将相应关键字进行高亮显示
                doc.setCharacterAttributes(lastIndex, keyword.length(), keywordStyle, true);
                //更新为当前关键词之后的索引位置
                lastIndex += keyword.length();
            }
        }
    }
*/

    // Java 关键字的高亮
    private static void highlightJava(JTextPane textPane, String text) {
        StyledDocument doc = textPane.getStyledDocument(); 

        // 创建不同样式的对象，用于不同类型的关键字
        Style defaultStyle = textPane.getStyle("default");

        Style keywordStyle = textPane.addStyle("JavaKeyword", defaultStyle);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);

        Style controlFlowStyle = textPane.addStyle("JavaControlFlow", defaultStyle);
        StyleConstants.setForeground(controlFlowStyle, new Color(0, 128, 0));//深绿色

        Style constantStyle = textPane.addStyle("JavaConstant", defaultStyle);
        StyleConstants.setForeground(constantStyle, Color.MAGENTA);

        Style typeStyle = textPane.addStyle("JavaType", defaultStyle);
        StyleConstants.setForeground(typeStyle, Color.RED);

        // Java 关键字的列表按照类别分类
        String[] keywords = {
            // 关键字
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",  
            "const",  "continue", "default", "do", "double", "else", "enum", "extends", "final",  
            "finally", "float", "for", "if", "goto", "implements", "import", "instanceof", "int", 
            "interface", "long", "native",  "new", "package", "private", "protected", "public", 
            "return", "short", "static",  "strictfp", "super", "switch", "synchronized", "this", 
            "throw", "throws",  "transient", "try", "void", "volatile", "while", "true", "false", "null",  
            "module", // Java 9 引入  
            "open", "opens", // Java 9 引入，用于模块系统  
            "requires", // Java 9 引入，用于模块系统  
            "transitive", // Java 9 引入，与模块系统一起使用  
            "var" // Java 10 引入的局部变量类型推断  
        };

        // 去重和排序关键字列表
        Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords)); // 使用集合去重
        String[] sortedKeywords = keywordSet.toArray(new String[0]); // 将 Set 转换为数组
        Arrays.sort(sortedKeywords); // 排序关键字列表

        // 根据不同的关键字类型设置不同的样式
        for (String keyword : sortedKeywords) {
            Style styleToApply = keywordStyle; // 默认为关键字样式
            
            // 根据关键字类型选择不同的样式
            if (isControlFlowJava(keyword)) {
                styleToApply = controlFlowStyle;
            } else if (isConstantJava(keyword)) {
                styleToApply = constantStyle;
            } else if (isTypeJava(keyword)) {
                styleToApply = typeStyle;
            }

            int index = text.indexOf(keyword);
            while (index >= 0) {
                // 判断关键字前后是否是单词边界
                if (isWordBoundaryJava(text, index, keyword.length())) {
                    doc.setCharacterAttributes(index, keyword.length(), styleToApply, true);
                }
                index = text.indexOf(keyword, index + keyword.length());
            }
        }
    }

    // 判断是否为 Java 控制流关键字
    private static boolean isControlFlowJava(String keyword) {
        String[] controlFlowKeywords = {"if", "else", "for", "while", "do", "switch", "case", "default"};
        return Arrays.asList(controlFlowKeywords).contains(keyword);
    }

    // 判断是否为 Java 常量
    private static boolean isConstantJava(String keyword) {
        String[] constantKeywords = {"true", "false", "null"};
        return Arrays.asList(constantKeywords).contains(keyword);
    }

    // 判断是否为 Java 类型关键字
    private static boolean isTypeJava(String keyword) {
        String[] typeKeywords = {"boolean", "byte", "short", "int", "long", "float", "double", "char", "void"};
        return Arrays.asList(typeKeywords).contains(keyword);
    }

    // 判断指定位置的关键字前后是否是单词边界
    private static boolean isWordBoundaryJava(String text, int index, int keywordLength) {
        // 判断前一个字符是否是单词边界（即是否为空格、标点符号等）
        boolean isLeftBoundary = index == 0 || !Character.isJavaIdentifierPart(text.charAt(index - 1));
        
        // 判断后一个字符是否是单词边界（即是否为空格、标点符号等）
        boolean isRightBoundary = index + keywordLength >= text.length() || !Character.isJavaIdentifierPart(text.charAt(index + keywordLength));
        
        return isLeftBoundary && isRightBoundary;
    }


/* 
    //Python 关键字的高亮
    private static void highlightPython(JTextPane textPane, String text) {

        //设置文本的高亮样式(蓝色)
        StyledDocument doc = textPane.getStyledDocument();
        Style keywordStyle = textPane.addStyle("Keyword", null);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);

        //Python 关键字的列表
        String[] keywords = {"False", "class", "finally", "is", "return", "None", "continue", "for", "lambda", 
                             "try", "True", "def", "from", "nonlocal", "while", "and", "del", "global", "not", 
                             "with", "as", "elif", "if", "or", "yield", "assert", "else", "import", "pass", 
                             "break", "except", "in", "raise"};

        //遍历文本，对每个Python关键字进行高亮显示                     
        for (String keyword : keywords) {
            int lastIndex = 0;
            while ((lastIndex = text.indexOf(keyword, lastIndex)) != -1) {
                doc.setCharacterAttributes(lastIndex, keyword.length(), keywordStyle, true);
                lastIndex += keyword.length();
            }
        }
    }
*/

    // Python 关键字的高亮
    private static void highlightPython(JTextPane textPane, String text) {
        StyledDocument doc = textPane.getStyledDocument(); 
        
        // 创建不同样式的对象，用于不同类型的关键字
        Style defaultStyle = textPane.getStyle("default");

        Style keywordStyle = textPane.addStyle("PythonKeyword", defaultStyle);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);

        Style controlFlowStyle = textPane.addStyle("PythonControlFlow", defaultStyle);
        StyleConstants.setForeground(controlFlowStyle, new Color(0, 128, 0));//深绿色

        Style constantStyle = textPane.addStyle("PythonConstant", defaultStyle);
        StyleConstants.setForeground(constantStyle, Color.MAGENTA);

        // Python 关键字的列表按照类别分类
        String[] keywords = {
            // 关键字
            "False", "class", "finally", "is", "return", "None", "continue", "lambda",   
            "try", "True", "def", "from", "nonlocal", "while", "as", "assert", "async",   
            "await", "break", "del", "elif", "else", "except", "if", "import", "in", "global",   
            "not", "or", "pass", "raise", "with", "yield",

            // 控制流关键字
            "for", "while", "return", "try", "except", "finally",

            // 常量
            "False", "True", "None"
        };

        // 去重和排序关键字列表
        Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords)); // 使用集合去重
        String[] sortedKeywords = keywordSet.toArray(new String[0]); // 将 Set 转换为数组
        Arrays.sort(sortedKeywords); // 排序关键字列表

        // 根据不同的关键字类型设置不同的样式
        for (String keyword : sortedKeywords) {
            Style styleToApply = keywordStyle; // 默认为关键字样式
            
            // 根据关键字类型选择不同的样式
            if (isControlFlowPython(keyword)) {
                styleToApply = controlFlowStyle;
            } else if (isConstantPython(keyword)) {
                styleToApply = constantStyle;
            }

            int lastIndex = 0;
            while ((lastIndex = text.indexOf(keyword, lastIndex)) != -1) {
                // 判断关键字前后是否是单词边界
                if (isWordBoundaryPython(text, lastIndex, keyword.length())) {
                    doc.setCharacterAttributes(lastIndex, keyword.length(), styleToApply, true);
                }
                lastIndex += keyword.length();
            }
        }
    }

    // 判断是否为 Python 控制流关键字
    private static boolean isControlFlowPython(String keyword) {
        String[] controlFlowKeywords = {"if", "else", "for", "while", "return", "try", "except", "finally"};
        return Arrays.asList(controlFlowKeywords).contains(keyword);
    }

    // 判断是否为 Python 常量
    private static boolean isConstantPython(String keyword) {
        String[] constantKeywords = {"False", "True", "None"};
        return Arrays.asList(constantKeywords).contains(keyword);
    }

    // 判断指定位置的关键字前后是否是单词边界
    private static boolean isWordBoundaryPython(String text, int index, int keywordLength) {
        // 判断前一个字符是否是单词边界（即是否为空格、标点符号等）
        boolean isLeftBoundary = index == 0 || !Character.isJavaIdentifierPart(text.charAt(index - 1));
        
        // 判断后一个字符是否是单词边界（即是否为空格、标点符号等）
        boolean isRightBoundary = index + keywordLength >= text.length() || !Character.isJavaIdentifierPart(text.charAt(index + keywordLength));
        
        return isLeftBoundary && isRightBoundary;
    }


/* 
    //C++ 关键字的高亮
    private static void highlightCpp(JTextPane textPane, String text) {
        
        //设置文本的高亮样式(蓝色)
        StyledDocument doc = textPane.getStyledDocument();
        Style keywordStyle = textPane.addStyle("Keyword", null);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);

        //C++ 关键字列表
        String[] keywords = {"alignas", "alignof", "and", "and_eq", "asm", "atomic_cancel", "atomic_commit", 
                             "atomic_noexcept", "auto", "bitand", "bitor", "bool", "break", "case", "catch", "char", 
                             "char8_t", "char16_t", "char32_t", "class", "compl", "concept", "const", "consteval", 
                             "constexpr", "constinit", "const_cast", "continue", "co_await", "co_return", 
                             "co_yield", "decltype", "default", "delete", "do", "double", "dynamic_cast", "else", 
                             "enum", "explicit", "export", "extern", "false", "float", "for", "friend", "goto", 
                             "if", "inline", "int", "long", "mutable", "namespace", "new", "noexcept", "not", 
                             "not_eq", "nullptr", "operator", "or", "or_eq", "private", "protected", "public", 
                             "reflexpr", "register", "reinterpret_cast", "requires", "return", "short", "signed", 
                             "sizeof", "static", "static_assert", "static_cast", "struct", "switch", "synchronized", 
                             "template", "this", "thread_local", "throw", "true", "try", "typedef", "typeid", 
                             "typename", "union", "unsigned", "using", "virtual", "void", "volatile", "wchar_t", 
                             "while", "xor", "xor_eq"};

        //遍历文本，对每个C++关键字进行高亮显示
        for (String keyword : keywords) {
            int lastIndex = 0;
            while ((lastIndex = text.indexOf(keyword, lastIndex)) != -1) {
                doc.setCharacterAttributes(lastIndex, keyword.length(), keywordStyle, true);
                lastIndex += keyword.length();
            }
        }
    }
*/
    // C++ 关键字的高亮
    private static void highlightCpp(JTextPane textPane, String text) {
        StyledDocument doc = textPane.getStyledDocument(); 
        
        // 创建不同样式的对象，用于不同类型的关键字
        Style defaultStyle = textPane.getStyle("default");

        Style keywordStyle = textPane.addStyle("CppKeyword", defaultStyle);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);

        Style controlFlowStyle = textPane.addStyle("CppControlFlow", defaultStyle);
        StyleConstants.setForeground(controlFlowStyle, new Color(0, 128, 0));//深绿色

        Style typeStyle = textPane.addStyle("CppType", defaultStyle);
        StyleConstants.setForeground(typeStyle, Color.RED);

        Style modifierStyle = textPane.addStyle("CppModifier", defaultStyle);
        StyleConstants.setForeground(modifierStyle, Color.ORANGE);

        Style constantStyle = textPane.addStyle("CppConstant", defaultStyle);
        StyleConstants.setForeground(constantStyle, Color.MAGENTA);


        // 使用 Set 来存储关键字，确保没有重复项
        Set<String> keywordSet = new HashSet<>();

        // 添加不同类别的关键字到 Set
        keywordSet.addAll(Arrays.asList(
            // 关键字
            "alignas", "alignof", "asm", "atomic_cancel", "atomic_commit", "atomic_noexcept", 
            "auto", "concept", "const", "consteval", "constexpr", "constinit", "decltype", 
            "delete", "dynamic_cast", "explicit", "export", "extern", "false", "friend", 
            "mutable", "namespace", "new", "noexcept", "operator", "private", "protected", 
            "public", "reflexpr", "register", "reinterpret_cast", "requires", "static", 
            "static_assert", "static_cast", "synchronized", "template", "this", "throw", 
            "true", "typedef", "typeid", "typename", "using", "virtual", "volatile", "wchar_t", 
            "std","include","iostream","cin","cout",

            // 控制流关键字
            "if", "else", "for", "while", "switch", "case", "default", "return", "try", 
            "catch", "finally", "co_await", "co_return", "co_yield", 

            // 类型关键字
            "bool", "char", "char8_t", "char16_t", "char32_t", "double", "float", "int", 
            "long", "short", "signed", "unsigned", "void", 

            // 修饰符
            "and", "and_eq", "bitand", "bitor", "compl", "not", "not_eq", "or", "or_eq", 
            "xor", "xor_eq", 

            // 常量
            "nullptr", "true", "false"
        ));

        // 将 Set 转换为数组
        String[] keywords = keywordSet.toArray(new String[0]);

        // 根据不同的关键字类型设置不同的样式
        for (String keyword : keywords) {
            Style styleToApply = keywordStyle; // 默认为关键字样式
            
            // 判断关键字类型并应用相应样式
            if (isControlFlowCpp(keyword)) {
                styleToApply = controlFlowStyle;
            } else if (isTypeCpp(keyword)) {
                styleToApply = typeStyle;
            } else if (isModifierCpp(keyword)) {
                styleToApply = modifierStyle;
            } else if (isConstantCpp(keyword)) {
                styleToApply = constantStyle;
            }

            int lastIndex = 0;
            while ((lastIndex = text.indexOf(keyword, lastIndex)) != -1) {
                // 判断关键字前后是否是单词边界
                if (isWordBoundary(text, lastIndex, keyword.length())) {
                    doc.setCharacterAttributes(lastIndex, keyword.length(), styleToApply, true);
                }
                lastIndex += keyword.length();
            }
        }
    }

    // 判断是否为 C++ 控制流关键字
    private static boolean isControlFlowCpp(String keyword) {
        String[] controlFlowKeywords = {"if", "else", "for", "while", "switch", "case", "default", "return", "try", "catch", "finally", "co_await", "co_return", "co_yield"};
        return Arrays.asList(controlFlowKeywords).contains(keyword);
    }

    // 判断是否为 C++ 类型关键字
    private static boolean isTypeCpp(String keyword) {
        String[] typeKeywords = {"bool", "char", "char8_t", "char16_t", "char32_t", "double", "float", "int", "long", "short", "signed", "unsigned", "void"};
        return Arrays.asList(typeKeywords).contains(keyword);
    }

    // 判断是否为 C++ 修饰符
    private static boolean isModifierCpp(String keyword) {
        String[] modifierKeywords = {"and", "and_eq", "bitand", "bitor", "compl", "not", "not_eq", "or", "or_eq", "xor", "xor_eq"};
        return Arrays.asList(modifierKeywords).contains(keyword);
    }

    // 判断是否为 C++ 常量
    private static boolean isConstantCpp(String keyword) {
        String[] constantKeywords = {"nullptr", "true", "false"};
        return Arrays.asList(constantKeywords).contains(keyword);
    }

    // 判断指定位置的关键字前后是否是单词边界
    private static boolean isWordBoundary(String text, int index, int keywordLength) {
        // 判断前一个字符是否是单词边界（即是否为空格、标点符号等）
        boolean isLeftBoundary = index == 0 || !Character.isJavaIdentifierPart(text.charAt(index - 1));
        
        // 判断后一个字符是否是单词边界（即是否为空格、标点符号等）
        boolean isRightBoundary = index + keywordLength >= text.length() || !Character.isJavaIdentifierPart(text.charAt(index + keywordLength));
        
        return isLeftBoundary && isRightBoundary;
    }
}

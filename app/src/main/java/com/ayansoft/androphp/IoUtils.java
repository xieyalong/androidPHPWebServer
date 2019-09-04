package com.ayansoft.androphp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoUtils {
  private static final int BUFFER_SIZE = 8192;
  
  public static int copy(InputStream paramInputStream, OutputStream paramOutputStream) throws Exception, IOException { // Byte code:
    //   0: sipush #8192
    //   3: newarray byte
    //   5: astore #4
    //   7: new java/io/BufferedInputStream
    //   10: dup
    //   11: aload_0
    //   12: sipush #8192
    //   15: invokespecial <init> : (Ljava/io/InputStream;I)V
    //   18: astore_0
    //   19: new java/io/BufferedOutputStream
    //   22: dup
    //   23: aload_1
    //   24: sipush #8192
    //   27: invokespecial <init> : (Ljava/io/OutputStream;I)V
    //   30: astore #5
    //   32: iconst_0
    //   33: istore_2
    //   34: aload_0
    //   35: aload #4
    //   37: iconst_0
    //   38: sipush #8192
    //   41: invokevirtual read : ([BII)I
    //   44: istore_3
    //   45: iload_3
    //   46: iconst_m1
    //   47: if_icmpne -> 66
    //   50: aload #5
    //   52: invokevirtual flush : ()V
    //   55: aload #5
    //   57: invokevirtual close : ()V
    //   60: aload_0
    //   61: invokevirtual close : ()V
    //   64: iload_2
    //   65: ireturn
    //   66: aload #5
    //   68: aload #4
    //   70: iconst_0
    //   71: iload_3
    //   72: invokevirtual write : ([BII)V
    //   75: iload_2
    //   76: iload_3
    //   77: iadd
    //   78: istore_2
    //   79: goto -> 34
    //   82: astore_1
    //   83: aload #5
    //   85: invokevirtual close : ()V
    //   88: aload_0
    //   89: invokevirtual close : ()V
    //   92: aload_1
    //   93: athrow
    //   94: astore #4
    //   96: ldc ''
    //   98: aload #4
    //   100: invokevirtual getMessage : ()Ljava/lang/String;
    //   103: aload #4
    //   105: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   108: pop
    //   109: goto -> 88
    //   112: astore_0
    //   113: ldc ''
    //   115: aload_0
    //   116: invokevirtual getMessage : ()Ljava/lang/String;
    //   119: aload_0
    //   120: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   123: pop
    //   124: goto -> 92
    //   127: astore_1
    //   128: ldc ''
    //   130: aload_1
    //   131: invokevirtual getMessage : ()Ljava/lang/String;
    //   134: aload_1
    //   135: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   138: pop
    //   139: goto -> 60
    //   142: astore_0
    //   143: ldc ''
    //   145: aload_0
    //   146: invokevirtual getMessage : ()Ljava/lang/String;
    //   149: aload_0
    //   150: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   153: pop
    //   154: iload_2
    //   155: ireturn
    // Exception table:
    //   from	to	target	type
    //   34	45	82	finally
    //   50	55	82	finally
    //   55	60	127	java/io/IOException
    //   60	64	142	java/io/IOException
    //   66	75	82	finally
    //   83	88	94	java/io/IOException
    //   88	92	112	java/io/IOException
      return  0;
      }
}


/* Location:              C:\反编译\dex2jar-2.0\dex2jar-2.0\classes-dex2jar.jar!\com\ayansoft\androphp\IoUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */

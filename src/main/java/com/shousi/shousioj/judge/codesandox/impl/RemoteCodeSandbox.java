package com.shousi.shousioj.judge.codesandox.impl;

import com.shousi.shousioj.judge.codesandox.CodeSandbox;
import com.shousi.shousioj.judge.codesandox.model.ExecuteCodeRequest;
import com.shousi.shousioj.judge.codesandox.model.ExecuteCodeResponse;

/**
 * 远程代码沙箱
 */
public class RemoteCodeSandbox implements CodeSandbox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}

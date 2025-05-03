package com.shousi.shousioj.judge.codesandox;

import com.shousi.shousioj.judge.codesandox.model.ExecuteCodeRequest;
import com.shousi.shousioj.judge.codesandox.model.ExecuteCodeResponse;

/**
 * 代码沙箱统一实现接口
 */
public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}

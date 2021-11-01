package com.ideaas.ecomm.ecomm.util;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class FileUtil {

    public static File loadEmployeesWithSpringInternalClass(final String path) throws FileNotFoundException {
        return ResourceUtils.getFile("classpath:".concat(path));
    }
}

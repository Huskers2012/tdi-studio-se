// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.exportjob.scriptsmanager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.repository.constants.FileConstants;

/**
 * Created by Marvin Wang on Mar 23, 2013.
 */
public class MapReduceJobJavaScriptsManager extends JobJavaScriptsManager {

    /**
     * DOC marvin MapReduceJobJavaScriptsManager constructor comment.
     * 
     * @param exportChoiceMap
     * @param contextName
     * @param launcher
     * @param statisticPort
     * @param tracePort
     */
    public MapReduceJobJavaScriptsManager(Map<ExportChoice, Object> exportChoiceMap, String contextName, String launcher,
            int statisticPort, int tracePort) {
        super(exportChoiceMap, contextName, launcher, statisticPort, tracePort);
    }

    /**
     * Overrides the parent method to do a special handling, that is to include "context" file in job.jar.
     * 
     * @return
     */
    @Override
    protected List<URL> getJobScripts(String projectName, String jobName, String jobVersion, boolean needJob) {
        List<URL> list = new ArrayList<URL>(1);
        if (!needJob) {
            return list;
        }

        try {
            String jobFolderName = JavaResourcesHelper.getJobFolderName(jobName, jobVersion);

            File jarFile = new File(getTmpFolder() + File.separatorChar + jobFolderName + FileConstants.JAR_FILE_SUFFIX);
            // Exports the jar file
            JarBuilder jarbuilder = new JarBuilder(getClassRootFileLocation(), jarFile);

            // builds the jar file of the job classes,needContext specifies whether inclucdes the context.
            // add the job
            String jobPath = projectName + PATH_SEPARATOR + jobFolderName;
            jarbuilder.setIncludeDir(Collections.singleton(jobPath));

            // Do not remove the context from job.jar.
            jarbuilder.setExcludeDir(null);
            jarbuilder.buildJar();
            list.add(jarFile.toURI().toURL());
        } catch (IOException e) {
            CommonExceptionHandler.process(e);
        } catch (CoreException e) {
            CommonExceptionHandler.process(e);
        }

        return list;
    }

}

package com.studio.zqquery.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.nyu.cs.javagit.api.GitFile;
import edu.nyu.cs.javagit.api.WorkingTree;
import edu.nyu.cs.javagit.api.commands.GitAdd;
import edu.nyu.cs.javagit.api.commands.GitAddResponse;
import edu.nyu.cs.javagit.api.commands.GitCommit;
import edu.nyu.cs.javagit.api.commands.GitCommitOptions;
import edu.nyu.cs.javagit.api.commands.GitCommitResponse;
import edu.nyu.cs.javagit.api.commands.GitInit;
import edu.nyu.cs.javagit.api.commands.GitInitResponse;
import edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit;
import edu.nyu.cs.javagit.api.commands.GitStatus;
import edu.nyu.cs.javagit.api.commands.GitStatusResponse;

public class HBNGitService {

	public static void main(String[] args) {

		try {
			File root = new File("E:/gitquery/2016/4/test/1460648312028");
			// GitInit gitInit = new GitInit();
			// GitInitResponse gitResponse = gitInit.init(root);
			// System.out.println(gitResponse.initialized);

			GitAdd gitAdd = new GitAdd();
			GitStatus gitStatus = new GitStatus();
			List<File> paths = new ArrayList<File>();

			File file = new File(
					"E:/gitquery/2016/4/test/1460648312028/info.txt");
			// File file2 = new File(
			// "E:/gitquery/2016/4/test/1460648312028/info2.txt");
			// gitAdd.add(root, file1);
			// paths.add(file1);
			// paths.add(file2);
			// GitAddOptions options = new GitAddOptions();
			// options.setVerbose(true);
			// GitAddResponse gitAddResponse = gitAdd.add(root,file);
			// System.out.println(gitAddResponse.getFileListSize());
			// GitCommit gitCommit = new GitCommit();
			// GitCommitOptions gOptions = new GitCommitOptions();
			// gOptions.setAuthor("hbn@test.com");
			//
			// gitCommit.commit(root,"第二次修改文件");

			// GitStatusResponse statusResponse = gitStatus.status(root);
			// System.out.println(statusResponse.getMessage());

			WorkingTree workingTree = WorkingTree.getInstance(root);
			GitFile gitFile = workingTree.getFile(root);
			List<Commit>commitList= gitFile.getLog();
			for(Commit c:commitList){
				System.out.println(c.getAuthor()+"="+c.getMessage()+"="+c.getDateString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

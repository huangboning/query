package com.studio.query.service;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.studio.query.common.Configure;
import com.studio.query.entity.Account;
import com.studio.query.util.StringUtil;

public class JGitService {
	Logger loger = Logger.getLogger(JGitService.class);

	public String getAccountEmail(Account currentAccount) {
		String accountEmail = "";
		if (StringUtil.isNullOrEmpty(currentAccount.getAccountEmail())) {
			accountEmail = currentAccount.getAccountName() + Configure.systemEmail;
		} else {
			accountEmail = currentAccount.getAccountEmail();
		}
		return accountEmail;
	}

	public void createAccountRepository(String rootStr, String yearStr, String monthStr, String name) {

		File root = new File(rootStr);
		if (!root.exists()) {
			root.mkdir();
		}
		root = new File(rootStr + "/" + yearStr);
		if (!root.exists()) {
			root.mkdir();
		}
		root = new File(rootStr + "/" + yearStr + "/" + monthStr);
		if (!root.exists()) {
			root.mkdir();
		}
		root = new File(rootStr + "/" + yearStr + "/" + monthStr + "/" + name);
		if (!root.exists()) {
			root.mkdir();
		}
	}

	public boolean initAccountGit(String path, Account currentAccount) {
		File root = new File(path);
		if (!root.exists()) {
			root.mkdir();
		}
		File gitF = new File(path + "/.git");
		if (!gitF.exists()) {
			try {
				Git git = Git.init().setDirectory(root).call();
				// 默认初始化创建info.txt文件，并且提交做为默认第一个版本
				File file = new File(path + "/info.txt");
				file.createNewFile();
				git.add().addFilepattern("info.txt").call();
				PersonIdent personIdent = new PersonIdent(currentAccount.getAccountName(),
						this.getAccountEmail(currentAccount));
				git.commit().setCommitter(personIdent).setMessage("系统初始化info.txt").call();
			} catch (Exception e) {
				e.printStackTrace();
				loger.info(e.toString());
				return false;
			}
		}
		return true;
	}

	public boolean jGitCommit(String path, Account currentAccount, String message) {
		try {
			File root = new File(path);
			Git git = Git.init().setDirectory(root).call();
			git.add().addFilepattern("info.txt").call();
			PersonIdent personIdent = new PersonIdent(currentAccount.getAccountName(),
					this.getAccountEmail(currentAccount));
			git.commit().setCommitter(personIdent).setMessage(message).call();
			return true;
		} catch (Exception e) {
			loger.info(e.toString());
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {

		try {

			File root = new File("E:/gitquery/2016/5/huangboning/1462632511804");

			Git git = Git.init().setDirectory(root).call();

			// git.add().addFilepattern("test.txt").call();
			// PersonIdent personIdent = new PersonIdent("huangboning",
			// "huangboning@test.com");
			// git.commit().setCommitter(personIdent)
			// .setMessage("系统初始化info.txt").call();
			// for (RevCommit revCommit : git.log().call()) {
			// System.out.println(revCommit.getName()+"="+
			// revCommit.getFullMessage());
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void initGit() throws Exception {
	// File root = new File("f:/gittest");
	// if (!root.exists())
	// root.mkdir();
	// File gitF = new File("f:/gittest/.git");
	// if (!gitF.exists()) {// 如果已经初始化过,那肯定有.git文件夹
	// // 初始化git库,相当于命令行的 git init
	// Git.init().setDirectory(root).call();
	// }
	// Git git = Git.open(root); // 打开git库
	//
	// // 好吧,随便写一个文件进去先
	//
	// // File newFile = new
	// // File("f:/gittest/"+System.currentTimeMillis()+".java");
	// // FileWriter fw = new FileWriter(newFile);
	// // fw.write(System.currentTimeMillis() + " ABC");
	// // fw.flush();
	// // fw.close();
	// // 添加文件咯,相当于 git add
	// git.add().addFilepattern("readmine.txt").call();
	//
	// // FileUtils.delete(new File("gitme/1367405378240.java"));
	//
	// // 然后当然是提交啦,相当于 git commit
	// // git.commit().setCommitter("2",
	// // "2@gmail.com").setMessage("2!").call();
	// // git.commit().setMessage("添加文件readmine.txt").call();
	// git.commit().setCommitter("hhc", "hhc@126.com")
	// .setMessage("修改readmine.txt 2").call();
	// // // 接下来,我们看看log信息
	// // for (RevCommit revCommit : git.log().call()) {
	// // System.out.println("----------------------");
	// // ObjectId objId = git.getRepository().resolve(revCommit.name()); //
	// // revCommit.name是版本号
	// // RevTree tree = revCommit.getTree(); // 这货API上说是用来存文件的，不知道怎么用
	// // if (objId != null) {
	// // System.out.println("objId is not null " + tree.getType());
	// // } else {
	// // System.out.println("objId is null");
	// // }
	// //
	// // System.out.println("::::::::");
	// //
	// // TreeWalk treewalk = TreeWalk.forPath(git.getRepository(),
	// // "1367648948527.java", tree);
	// // if (treewalk != null) {
	// // byte[] data = git.getRepository().open(treewalk.getObjectId(0))
	// // .getBytes();
	// // System.out.println(data.length);
	// // } else {
	// // System.out.println("No~!");
	// // }
	// // /*
	// // * Iterable<RevCommit> allCommitsLater =
	// // * git.log().add(objId).call(); Iterator<RevCommit> iter =
	// // * allCommitsLater.iterator(); RevCommit commit = iter.next();
	// // * TreeWalk tw = new TreeWalk(git.getRepository());
	// // * tw.addTree(commit.getTree()); commit = iter.next(); if (commit !=
	// // * null) { tw.addTree(commit.getTree()); } else {
	// // * System.out.println("当前库只有一个版本，不能获取变更记录"); continue; }
	// // *
	// // * tw.setRecursive(true); RenameDetector rd = new
	// // * RenameDetector(git.getRepository());
	// // * rd.addAll(DiffEntry.scan(tw)); List<DiffEntry> diffEntries =
	// // * rd.compute(); if (diffEntries == null || diffEntries.size() == 0)
	// // * { System.out.println("no change"); continue; }
	// // * Iterator<DiffEntry> iterator = new
	// // * ArrayList<DiffEntry>(diffEntries).iterator(); DiffEntry diffEntry
	// // * = null; while (iterator.hasNext()) { diffEntry = iterator.next();
	// // * System.out.println("newPath:" + diffEntry.getNewPath() +
	// // * " oldPath:" + diffEntry.getOldPath() + " changeType:" +
	// // * diffEntry.getChangeType()); if (diffEntry.getChangeType() ==
	// // * ChangeType.DELETE) { iterator.remove(); } }
	// // */
	// //
	// // System.out.println(":::::::::");
	// //
	// // System.out.println("BranchName\t"
	// // + git.getRepository().getFullBranch());
	// // System.out.println("WorkTree\t"
	// // + git.getRepository().getWorkTree().getPath());
	// // System.out.println("revCommit\t" + revCommit);
	// // System.out.println("CommitTime\t" + revCommit.getCommitTime());
	// // System.out.println("name\t\t" + revCommit.getName());
	// // System.out.println("ShortMessage\t" + revCommit.getShortMessage());
	// // System.out.println("Type\t\t" + revCommit.getType());
	// // System.out.println("line\t\t" + revCommit.getFooterLines().size());
	// // System.out.println("Tree\t\t" + revCommit.getTree());
	// // System.out.println("FullMessage\t" + revCommit.getFullMessage());
	// // System.out.println("Committer\t"
	// // + revCommit.getCommitterIdent().getName() + " "
	// // + revCommit.getCommitterIdent().getEmailAddress());
	// // System.out.println("----------------------");
	// //
	// // }
	// // System.out.println(getContentWithFile("gitme", "",
	// // "1367580635316.java"));
	// }

	// String getContentWithFile(String gitRoot, final String branchName, String
	// fileName) throws Exception {
	public static String getContentWithFile(String gitRoot, String branchName, String fileName) throws Exception {
		final Git git = Git.open(new File(gitRoot));
		Repository repository = git.getRepository();

		repository = git.getRepository();
		RevWalk walk = new RevWalk(repository);
		Ref ref = repository.getRef(branchName);

		ObjectId objId = ref.getObjectId();
		RevCommit revCommit = walk.parseCommit(objId);
		RevTree revTree = revCommit.getTree();

		TreeWalk treeWalk = TreeWalk.forPath(repository, fileName, revTree);
		// 文件名错误
		if (treeWalk == null)
			return null;

		ObjectId blobId = treeWalk.getObjectId(0);
		ObjectLoader loader = repository.open(blobId);
		byte[] bytes = loader.getBytes();
		if (bytes != null)
			return new String(bytes);
		return null;
	}

}
package com.studio.query.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import com.studio.query.common.Configure;
import com.studio.query.entity.Account;
import com.studio.query.entity.Committer;
import com.studio.query.util.DateUtil;
import com.studio.query.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
		// 创建用户场景的版本库
		root = new File(rootStr + "/" + yearStr + "/" + monthStr + "/" + name + "/scene");
		if (!root.exists()) {
			root.mkdir();
		}
		// 创建用户共享fragment的版本库
		root = new File(rootStr + "/" + yearStr + "/" + monthStr + "/" + name + "/shareFragment");
		if (!root.exists()) {
			root.mkdir();
		}
	}

	/**
	 * 初始化用户的版本库
	 * 
	 * @param path
	 * @param currentAccount
	 * @return
	 */
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

	/**
	 * 初始化共享fragment版本库
	 * 
	 * @param path
	 * @param currentAccount
	 * @return
	 */
	public boolean initShareFragmentGit(String path, Account currentAccount) {
		File root = new File(path);
		if (!root.exists()) {
			root.mkdir();
		}
		File gitF = new File(path + "/.git");
		if (!gitF.exists()) {
			try {
				Git.init().setDirectory(root).call();
				File file = new File(path + "/template.txt");
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				loger.info(e.toString());
				return false;
			}
		}
		return true;
	}

	/**
	 * 初始化共享变量版本库
	 * 
	 * @param path
	 * @param currentAccount
	 * @return
	 */
	public boolean initShareVariableGit(String path, Account currentAccount) {
		File root = new File(path);
		if (!root.exists()) {
			root.mkdir();
		}
		File gitF = new File(path + "/.git");
		if (!gitF.exists()) {
			try {
				Git.init().setDirectory(root).call();
				File file = new File(path + "/template.txt");
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				loger.info(e.toString());
				return false;
			}
		}
		return true;
	}

	public static Repository openRepository(String path) {

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		builder.setMustExist(true);
		Repository repository = null;
		try {
			repository = builder.setGitDir(new File(path + "/.git")).build();
		} catch (IOException e) {
			e.printStackTrace();

		}
		return repository;
	}

	/**
	 * git提交版本
	 * 
	 * @param path
	 * @param currentAccount
	 * @param message
	 * @return
	 */
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

	/**
	 * checkout
	 * 
	 * @param path
	 * @param version
	 * @return
	 */
	public boolean jGitCheckout(String path, String version) {
		try {
			File root = new File(path);
			Git git = Git.init().setDirectory(root).call();
			git.checkout().setName(version).call();
			return true;
		} catch (Exception e) {
			loger.info(e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * jgit创建分支
	 * 
	 * @param path
	 * @param branchName
	 * @return
	 */
	public boolean jGitCreateBranch(String path, String branchName) {
		try {
			File root = new File(path);
			Git git = Git.init().setDirectory(root).call();
			git.checkout().setCreateBranch(true).setName(branchName).call();
			return true;
		} catch (Exception e) {
			loger.info(e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 提交共享fragment(发布共享的时候初始化template.txt并提交发布的内容)
	 * 
	 * @param path
	 * @param currentAccount
	 * @param message
	 * @return
	 */
	public boolean shareFragmentCommit(String path, Account currentAccount, String message) {
		try {
			File templateFile = new File(path + "/template.txt");
			if (!templateFile.exists()) {
				File file = new File(path + "/template.txt");
				file.createNewFile();
			}
			File root = new File(path);
			Git git = Git.init().setDirectory(root).call();
			git.add().addFilepattern("template.txt").call();
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

	/**
	 * 提交共享变量(发布共享的时候初始化template.txt并提交发布的内容)
	 * 
	 * @param path
	 * @param currentAccount
	 * @param message
	 * @return
	 */
	public boolean shareVariableCommit(String path, Account currentAccount, String message) {
		try {
			File templateFile = new File(path + "/template.txt");
			if (!templateFile.exists()) {
				File file = new File(path + "/template.txt");
				file.createNewFile();
			}
			File root = new File(path);
			Git git = Git.init().setDirectory(root).call();
			git.add().addFilepattern("template.txt").call();
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

	/**
	 * 获取最后一次提交者信息
	 * 
	 * @param path
	 * @return
	 */
	public Committer getLastCommitter(String path) {
		Committer committerInfo = new Committer();
		try (Repository repository = JGitService.openRepository(path)) {

			ObjectId lastCommitId = repository.resolve(Constants.HEAD);
			if (null == lastCommitId) {

			}
			try (RevWalk revWalk = new RevWalk(repository)) {
				RevCommit commit = revWalk.parseCommit(lastCommitId);
				committerInfo.setCommitVersion(commit.getName());
				committerInfo.setCommitName(commit.getAuthorIdent().getName());
				committerInfo.setCommitEmail(commit.getAuthorIdent().getEmailAddress());
				committerInfo.setCommitDate(DateUtil.dateTimeFormat(commit.getAuthorIdent().getWhen()));
				committerInfo.setCommitMssage(commit.getFullMessage());
				revWalk.dispose();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return committerInfo;
	}

	/**
	 * 根据版本号获取提交者信息
	 * 
	 * @param path
	 * @return
	 */
	public Committer getCommitterByVersion(String path, String version) {
		Committer committerInfo = new Committer();
		try (Repository repository = JGitService.openRepository(path)) {

			ObjectId lastCommitId = repository.resolve(version);
			if (null == lastCommitId) {

			}
			try (RevWalk revWalk = new RevWalk(repository)) {
				RevCommit commit = revWalk.parseCommit(lastCommitId);
				committerInfo.setCommitVersion(commit.getName());
				committerInfo.setCommitName(commit.getAuthorIdent().getName());
				committerInfo.setCommitEmail(commit.getAuthorIdent().getEmailAddress());
				committerInfo.setCommitDate(DateUtil.dateTimeFormat(commit.getAuthorIdent().getWhen()));
				committerInfo.setCommitMssage(commit.getFullMessage());
				revWalk.dispose();
			}
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
		return committerInfo;
	}

	/**
	 * 根据版本获取内容
	 * 
	 * @param path
	 * @param version
	 * @param fileName
	 * @return
	 */
	public String getContentByVersion(String path, String version, String fileName) {
		String str = "";
		try (Repository repository = JGitService.openRepository(path)) {

			ObjectId lastCommitId = repository.resolve(version);
			if (null == lastCommitId) {
				return str;
			}
			try (RevWalk revWalk = new RevWalk(repository)) {
				RevCommit commit = revWalk.parseCommit(lastCommitId);
				// and using commit's tree find the path
				RevTree tree = commit.getTree();
				// System.out.println("Having tree: " + tree);

				// now try to find a specific file
				try (TreeWalk treeWalk = new TreeWalk(repository)) {
					treeWalk.addTree(tree);
					treeWalk.setRecursive(true); // 可以自动读取子树
					treeWalk.setFilter(PathFilter.create(fileName));
					if (treeWalk.next()) {

						ObjectId objectId = treeWalk.getObjectId(0);
						ObjectLoader loader = repository.open(objectId);
						str = new String(loader.getBytes(), "utf-8");

					}
				}

				revWalk.dispose();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return str;
	}

	public Map<String, String> initBranchHead(String path) {

		Map<String, String> branchHeadMap = new HashMap<String, String>();
		try (Repository repository = JGitService.openRepository(path)) {
			Git git = new Git(repository);
			List<Ref> branchList = git.branchList().call();
			for (Ref branch : branchList) {
				String branchName = branch.getName();
				boolean isDetachedBranch = false;
				// if (branchName.equals("HEAD")) {
				// isDetachedBranch = true;
				// }
				if (!branchName.startsWith(Constants.R_HEADS)) {
					if (!isDetachedBranch) {
						continue;
					}
				}

				String shortBranchName = branchName.substring(branchName.lastIndexOf("/") + 1);
				git.checkout().setName(shortBranchName).call();
				ObjectId lastCommitId = repository.resolve(Constants.HEAD);
				String headVersion = "";
				if (null != lastCommitId) {
					try (RevWalk revWalk = new RevWalk(repository)) {
						RevCommit commit = revWalk.parseCommit(lastCommitId);
						headVersion = commit.getName();
						revWalk.dispose();
					}
				}
				branchHeadMap.put(shortBranchName, headVersion);
			}
			git.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return branchHeadMap;
	}

	public boolean isLastVersion(String path, String version) {
		boolean result = false;
		Map<String, String> branchHeadMap = this.initBranchHead(path);
		for (Iterator i = branchHeadMap.keySet().iterator(); i.hasNext();) {
			Object obj = i.next();
			if (version.equals(branchHeadMap.get(obj))) {
				result = true;
				break;
			}

		}
		return result;
	}

	public String getBranchFromCommit(String path, String version, Map branchMapping) {
		String branchName = "";
		Map m = this.readLogTree(path, branchMapping);
		JSONObject mJson = new JSONObject().fromObject(m);
		JSONObject commitObj = mJson.getJSONObject("commit");
		JSONObject vObj = commitObj.getJSONObject(version);
		if (vObj != null) {
			JSONArray bArray = vObj.getJSONArray("branch");
			if (bArray.size() >= 1) {
				branchName = (String) bArray.get(0);
			}
		}
		return branchName;
	}

	public static void main(String[] args) {
		JGitService j = new JGitService();
		System.out.println(j.getBranchFromCommit("D:/gitquery/2016/5/huangboning/scene/1465013483313",
				"daf40c4b00ba8c82225e46b30f424423fe15e36b", new HashMap<>()));
		// Map m =
		// j.readLogTree("D:/gitquery/2016/5/huangboning/scene/1465013483313",
		// new HashMap<>());
		// JSONObject mJson = new JSONObject().fromObject(m);
		// System.out.println(mJson);
		// System.out.println(j.isLastVersion("D:/gitquery/2016/5/huangboning/scene/1465013483313",
		// "87270e1d78453978b120e01c3d665e384fb9d42b"));
	}

	/**
	 * 版本提交记录
	 * 
	 * @param path
	 * @return
	 */
	public List<Committer> getLogs(String path) {
		List<Committer> committerList = new ArrayList<Committer>();
		try (Repository repository = JGitService.openRepository(path)) {
			Git git = new Git(repository);
			for (RevCommit revCommit : git.log().call()) {
				Committer committerInfo = new Committer();
				committerInfo.setCommitVersion(revCommit.getName());
				committerInfo.setCommitName(revCommit.getAuthorIdent().getName());
				committerInfo.setCommitEmail(revCommit.getAuthorIdent().getEmailAddress());
				committerInfo.setCommitMssage(revCommit.getFullMessage());
				committerInfo.setCommitDate(DateUtil.dateTimeFormat(revCommit.getAuthorIdent().getWhen()));
				committerList.add(committerInfo);
			}
			git.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return committerList;
	}

	public static void recursiveRead(String parentName, Map<String, List<String>> commitTree,
			Map<String, Integer> sortedCommitMap, int level) {
		int currentLevel = 0;
		List<String> childList = commitTree.get(parentName);
		for (String commit : childList) {
			sortedCommitMap.put(commit, level);
		}

		currentLevel = level + 1;
		for (String commit : childList) {
			if (commitTree.containsKey(commit)) {
				recursiveRead(commit, commitTree, sortedCommitMap, currentLevel);
			}
		}
	}

	public static Map readLogTree(String relativePath, Map branchMapping) {
		Map historyMap = new HashMap();
		List branchList = new ArrayList();
		Map branchMap = null;
		List<Map> commitList = null;
		Map commitMap = null;
		Map<String, Integer> sortedCommitMap = new HashMap<String, Integer>();
		Set<String> uniqueCommitSet = new HashSet<String>();
		List<RevCommit> allCommitList = null;
		String shortBranchName = null;
		Map<String, List<String>> commitTree = new HashMap();
		List<String> childList = new ArrayList();
		String parentName = null;

		try {
			Repository repository = openRepository(relativePath);
			Git git = new Git(repository);

			RevWalk walk = new RevWalk(repository);

			// read HEAD
			ObjectId head = repository.resolve(Constants.HEAD);
			if (null == head)
				return null;
			RevCommit headCommit = walk.parseCommit(head);

			Iterable<RevCommit> commits = git.log().all().call();
			// .addPath("aed1669c345345b3acbb6142274573b2.json")

			// 统计总提交数 以及每个提交的顺序
			allCommitList = new ArrayList();
			int i = 0;
			for (RevCommit commit : commits) {
				i++;
				// logMap.put(commit.getName(), i);
				allCommitList.add(commit);

				if (commit.getParentCount() != 0) {
					for (RevCommit parent : commit.getParents()) {
						parentName = parent.getName();
						if (commitTree.containsKey(parentName)) {
							childList = commitTree.get(parentName);
						} else {
							childList = new ArrayList();
						}
					}
				} else {
					parentName = "root";
					childList = new ArrayList();
				}
				childList.add(commit.getName());
				commitTree.put(parentName, childList);
			}
			recursiveRead("root", commitTree, sortedCommitMap, 1);
			List<Ref> branches = git.branchList().call();
			commitList = new ArrayList();

			List<String> masterList = new ArrayList<String>();
			for (Ref branch : branches) {
				String branchName = branch.getName();
				boolean isDetachedBranch = false;
				if (branchName.equals("HEAD")) {
					isDetachedBranch = true;
				}
				if (!branchName.startsWith(Constants.R_HEADS)) {
					if (!isDetachedBranch) {
						continue;
					}
				}

				shortBranchName = branchName.substring(branchName.lastIndexOf("/") + 1);
				masterList.add(shortBranchName);
				branchMap = new HashMap();
				// commitList = new ArrayList();
				for (RevCommit commit : allCommitList) {
					boolean foundInThisBranch = false;
					RevCommit targetCommit = walk.parseCommit(repository.resolve(commit.getName()));
					// System.out.println("targetCommit: "+ targetCommit);
					Map<String, Ref> testRef = repository.getAllRefs();
					for (Map.Entry<String, Ref> e : testRef.entrySet()) {
						if (e.getKey().startsWith(Constants.R_HEADS)) {
							if (walk.isMergedInto(targetCommit, walk.parseCommit(e.getValue().getObjectId()))) {
								String foundInBranch = e.getValue().getName();
								if (branchName.equals(foundInBranch)) {
									foundInThisBranch = true;
									break;
								}
							}
						} else if (isDetachedBranch) {
							if (walk.isMergedInto(targetCommit, walk.parseCommit(e.getValue().getObjectId()))) {
								String foundInBranch = e.getValue().getName();
								if (branchName.equals(foundInBranch)) {
									foundInThisBranch = true;
									break;
								}
							}
						}
					}
					if (foundInThisBranch) {
						if (headCommit.getName().equals(commit.getName())) {
							commitMap = new HashMap();
							commitMap.put("id", headCommit.getName());
							commitMap.put("branch", shortBranchName);
							commitMap.put("comment", headCommit.getShortMessage());
							commitMap.put("committer", headCommit.getCommitterIdent().getName());
							commitMap.put("commiteEmail", headCommit.getCommitterIdent().getEmailAddress());
							commitMap.put("commitDate",
									DateUtil.dateTimeFormat(headCommit.getCommitterIdent().getWhen()));
							historyMap.put("current", commitMap);
						}
						int position = sortedCommitMap.get(commit.getName());
						commitMap = new HashMap();
						commitMap.put("id", commit.getName());
						commitMap.put("position", position);
						commitMap.put("commitDate", DateUtil.dateTimeFormat(commit.getCommitterIdent().getWhen()));
						int oop = commit.getParents().length;
						if (oop > 0) {
							commitMap.put("parent",
									commit.getParents()[commit.getParents().length - 1].getName().toString());
						} else {
							historyMap.put("root", commitMap);
						}
						List<String> branchNameList = new ArrayList<String>();
						branchNameList.add(shortBranchName);
						if (oop > 0) {
							commitMap.put("branch", branchNameList);
						} else {
							commitMap.put("branch", masterList);
						}

						boolean isAdd = true;
						if (commitList.size() == 0) {
							commitList.add(commitMap);
						} else {
							for (int y = 0; y < commitList.size(); y++) {
								Map map = (Map) commitList.get(y);
								String id = (String) map.get("id");
								if (commit.getName().equals(id)) {
									List<String> bName = (List) map.get("branch");
									if (!bName.contains(shortBranchName)) {
										bName.add(shortBranchName);
										map.put("branch", bName);
										commitList.remove(y);
										commitList.add(y, map);
										isAdd = false;
										break;
									}
								}
							}
							if (isAdd) {
								commitList.add(commitMap);
							}
						}
					}
				}
			}
			changeResult(commitList, commitTree, branchMapping);
			Map nMap = new HashMap();
			for (int y = 0; y < commitList.size(); y++) {
				Map eachMap = commitList.get(y);
				String id = (String) eachMap.get("id");
				nMap.put(id, eachMap);
			}

			Set<String> keys = nMap.keySet();
			keys.forEach(key -> {
				Map lMap = (Map) nMap.get(key);
				List<String> childrens = (List) lMap.get("children");
				for (int j = 0; j < childrens.size(); j++) {
					Map cMap = (Map) nMap.get(childrens.get(j));
					List<String> branchNames = (List) cMap.get("branch");
					if (branchNames.contains("master")) {
						if (j == 0) {
							continue;
						} else {
							childrens.add(0, childrens.get(j));
							childrens.remove(j + 1);
							break;
						}
					} else {
						continue;
					}
				}
			});

			historyMap.put("commit", nMap);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (GitAPIException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return historyMap;
	}

	private static void changeResult(List<Map> commitList, Map<String, List<String>> commitTree, Map branchMappingMap) {

		for (int s = 0; s < commitList.size(); s++) {
			Map map = (Map) commitList.get(s);
			String id = (String) map.get("id");
			for (int b = 0; b < commitTree.size(); b++) {
				List<String> kMap = (List) commitTree.get(id);
				if (kMap == null) {
					map.put("children", new ArrayList());
					List<String> branchList = (List) map.get("branch");
					int y = -1;
					for (int u = 0; u < branchList.size(); u++) {
						if (branchList.get(u).equals("HEAD")) {
							y = u;
						}
					}
					if (y != -1) {
						branchList.remove(y);
					}
					List bS = new ArrayList();
					String gitBranchName = branchList.get(0);
					if ("master".equals(gitBranchName)) {
						bS.add("master");
						map.put("branch", bS);
					} else {
						String inputS = (String) branchMappingMap.get(gitBranchName);
						if (null == inputS) {
							inputS = gitBranchName;
						}
						bS.add(inputS);
						map.put("branch", bS);
					}
				} else {
					map.put("children", kMap);
				}
				commitList.remove(s);
				commitList.add(s, map);
			}
		}
	}

	public static void main222(String[] args) {

		// Map map =
		// readLogTree("D:/gitquery/2016/5/huangboning/scene/1465013483313", new
		// HashMap<>());
		// System.out.println(map.get("current"));
		// JSONObject obj = new JSONObject();
		// obj.put("baseObject", map);
		// System.out.println(obj.toString());

		try (Repository repository = JGitService.openRepository("D:/gitquery/2016/5/huangboning/scene/1465013483313")) {
			Git git = new Git(repository);
			List<Ref> branchList = git.branchList().call();
			for (Ref branch : branchList) {
				String branchName = branch.getName();
				boolean isDetachedBranch = false;
				// if (branchName.equals("HEAD")) {
				// isDetachedBranch = true;
				// }
				if (!branchName.startsWith(Constants.R_HEADS)) {
					if (!isDetachedBranch) {
						continue;
					}
				}

				String shortBranchName = branchName.substring(branchName.lastIndexOf("/") + 1);
				System.out.println(shortBranchName);
				git.checkout().setName(shortBranchName).call();
				ObjectId lastCommitId = repository.resolve(Constants.HEAD);
				if (null != lastCommitId) {
					try (RevWalk revWalk = new RevWalk(repository)) {
						RevCommit commit = revWalk.parseCommit(lastCommitId);
						System.out.println(commit.getName());
						revWalk.dispose();
					}
				}

				// for (RevCommit revCommit : git.log().call()) {
				// System.out.println(revCommit.getName());
				// }
			}
			// for (RevCommit revCommit : git.log().call()) {
			// // ObjectId objId =
			// // git.getRepository().resolve(revCommit.name());
			//
			// System.out.println(revCommit.getName());
			// }
			git.close();
		} catch (Exception e) {

			e.printStackTrace();
		}

		// try (Repository repository =
		// JGitService.openRepository("E:/gittest")) {
		// // find the HEAD
		// ObjectId lastCommitId =
		// repository.resolve("a21cf0c64d5a308676425c816777891990522b3f");
		// if (null == lastCommitId) {
		//
		// }
		// try (RevWalk revWalk = new RevWalk(repository)) {
		// RevCommit commit = revWalk.parseCommit(lastCommitId);
		// // and using commit's tree find the path
		// RevTree tree = commit.getTree();
		// // System.out.println("Having tree: " + tree);
		//
		// // now try to find a specific file
		// try (TreeWalk treeWalk = new TreeWalk(repository)) {
		// treeWalk.addTree(tree);
		// treeWalk.setRecursive(true); // 可以自动读取子树
		// treeWalk.setFilter(PathFilter.create("readme.txt"));
		// if (treeWalk.next()) {
		//
		// ObjectId objectId = treeWalk.getObjectId(0);
		// ObjectLoader loader = repository.open(objectId);
		// String str = new String(loader.getBytes(), "UTF-8");
		// System.out.println(str);
		//
		// }
		// }
		//
		// revWalk.dispose();
		// }
		// } catch (IOException e) {
		//
		// e.printStackTrace();
		// }

		// try {
		//
		// File root = new File("E:/gittest");
		//
		// Repository repository = JGitService.openRepository("E:/gittest");
		// ObjectId lastCommitId = repository.resolve(Constants.HEAD);
		// ObjectLoader loader = repository.open(lastCommitId);
		// String str = new String(loader.getBytes(), "UTF-8");
		// System.out.println(str);
		// // Git git = Git.init().setDirectory(root).call();
		// // Ref
		// //
		// ref=git.checkout().setName("015e0b852e0ce5841aa03ef1ec077932684e372a").call();
		// // Ref ref=git.checkout().setName("master").call();
		// // System.out.println(ref.getName());
		// // FileRepositoryBuilder builder = new FileRepositoryBuilder();
		// // builder.setMustExist(true);
		// // Repository repository = builder.setGitDir(new
		// // File("E:/gittest/.git")).build();
		// // ObjectId lastCommitId = repository.resolve(Constants.HEAD);
		// // RevWalk revWalk = new RevWalk(repository);
		// // RevCommit commit = revWalk.parseCommit(lastCommitId);
		// // String str = commit.getName();
		// // System.out.println(commit.getParents()[0].getName());
		//
		// // git.add().addFilepattern("test.txt").call();
		// // PersonIdent personIdent = new PersonIdent("huangboning",
		// // "huangboning@test.com");
		// // git.commit().setCommitter(personIdent)
		// // .setMessage("系统初始化info.txt").call();
		// // for (RevCommit revCommit : git.log().call()) {
		// // System.out.println(revCommit.getName()+"="+
		// // revCommit.getFullMessage());
		// // }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
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

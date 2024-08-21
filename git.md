## Git command

### Configuration
```console
git config <--global> user.name "Your Name"
git config <--global> user.email "your.email@example.com"

# For example, to add --rebase as default of git pull
# .git/config or ~/.gitconfig
[branch "master"]
  remote = origin
  merge = refs/heads/master
  rebase = true
```
### Starting a Repository
```console
# Create an empty Git repository or reinitialize an existing one.
git init

### Staging change
```console
git add <file / folder>
git add .
```
### Un-staging change
```console
# unstage all (softly) 
git reset HEAD

# unstage specific
git rm <-rf> --cached <file/ folder>
```
### Show the status of working directory and staging area
```console
git status
```
### Commit changes to the repository
```console
git commit -m "Commit message"
```
### Inspecting & Comparing
```console
# show log
git log
# last <count>
git log -p <count>
# Shows commits after a specified time
git log -since=<time>
# Shows commits before a specified time
git log -until=<time>
# prettify
git log --online

# get current <commit hash>
git rev-parse HEAD
# to get shorter hash:
git rev-parse --short HEAD

# show diff compared with stage changes
git diff
# show diff between two commits
git diff <commit1> <commit2>
# create .patch (result from stage changes)
git add .
git diff --cached > <patch file>
# Apply the patch
git apply --stat <patch file>
git apply --check <patch file>
git apply <patch file>

# show diff (contains commit information)
git format-patch <branch1> <branch2>
# create .patch (--stdout for single file)
git format-patch <branch1> <branch2> --stdout > <patch file>
# Create patches for the last 3 commits
git format-patch -3
# Create a patch for a specific commit
git format-patch -1 <commit hash>
# Apply the patch
git am <patch file>
```
### Branching & Merging
```console
# List branches
git branch <-a>
# Create branch
git branch <branchname>

# Switch branch
git checkout <branchname>
# Create branch
git checkout -b <newbranch>
# Checkout and Create branch
git checkout -t <origin/kirkstone> -b <my-kirkstone>

# To merge <feature-branch> into <main>
git checkout <main>
git merge <feature-branch>
```
### Remote Repositories
```console
# list remotes
git remote -v
or
git branch -r
# add a remote repository
git remote add <new branch name> <url>

# Download objects and refs from another repository
git fetch <remote>

# Fetch from and integrate with another repository or a local branch.
git pull <remote> <branch>
# special note:
# 如果你有未提交的更改，则 git pull 命令的合并部分将失败，而你的本地分支将保持不变。
# 有 conflict 怎麼辦? 出現 conflict 一會暫停動作，需要你手動修復後，然後才可以繼續動作。
# 要整齊一點的紀錄，使用git pull --rebase
# 預期會有較多的 conflict，建議用 merge 或多開 branch; 如果修改範圍較小，不太預期有 conflict，則建議可以加上 rebase 參數。

# Clone
git clone -b <branch> <url> <output folder name>

# Update remote
git push <remote> <branch>
git push origin main
```
### Stash changes to a dirty working directory
```console
# stash
git stash 

# stash apply
git stash apply
```
### Reset / Revert
```console
# Moves the HEAD to <commit>.
# Keeps changes in the working directory.
# Changes remain staged. 
git reset --soft <commit>:
git reset --soft HEAD~1

# (default behavior)
# Moves the HEAD to <commit>.
# Resets the index to match <commit>.
# Changes are kept in the working directory but not staged.
git reset --mixed <commit>
git reset --mixed HEAD~1

# Moves the HEAD to <commit>.
# Resets the index and working directory to match <commit>.
# Discards all changes and staged files. 
git reset --hard <commit>
git reset --hard HEAD~1

# Unstages <file>, but preserves its contents. git reset HEAD index.html
git reset HEAD <file>:

# Creates a new commit that undoes the changes made by <commit>.
git revert <commit>:
```
### HEAD
```console
# refers to the state of HEAD two moves ago in the reflog.
HEAD@{2}
# refers to the commit that is two parents before the current HEAD.
HEAD~2
```
### Archive
```console
# git archive 是一个用于创建 Git 仓库中某个提交（commit）或分支（branch）的归档文件的命令。它生成一个包含指定内容的归档文件，比如 ZIP 或 TAR 文件。这个功能很有用，例如当你需要将某个特定版本的代码打包分发给其他人时。
git archive --format=zip --output=archive.zip HEAD
git archive --format=tar --output=archive.tar HEAD
git archive --format=zip --output=archive.zip <commit-id>
git archive --format=zip --output=archive.zip HEAD path/to/directory
```
### supplement: how to set git clone with ssh
https://tsengbatty.medium.com/git-%E8%B8%A9%E5%9D%91%E7%B4%80%E9%8C%84-%E4%BA%8C-git-clone-with-ssh-keys-%E6%88%96-https-%E8%A8%AD%E5%AE%9A%E6%AD%A5%E9%A9%9F-bdb721bd7cf2
### supplement: git push 
https://docs.github.com/en/get-started/using-git/pushing-commits-to-a-remote-repository

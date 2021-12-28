## Jennica's Rules to the GitHub
- To merge to the main repository you need permission from three others.
  -  
- To merge you must have all conversations on your code resolved.
- Commits should be logical and/or informative.

## Steps from new branch to merge
1. `$ git init`
2. Create new local branch:
   ```
   $ git checkout -b <new_branch_name>
   ```
3. Create new Branch in GitHub
4. Pull the current GitHub main branch locally:
   ```
   $ git pull https://github.com/JennicaLeClerc/Synergy_Project2.git <new_branch_name>
   ```
5. DO WHAT YOU NEED TO DO! (your current task) Committing as you go... I see you Ryan and John...
6. Checkout the your local main branch:
   ```
   $ git checkout main
   ```
7. Pull the current GitHub main branch to your local main branch:
   ```
   $ git pull https://github.com/JennicaLeClerc/Synergy_Project2.git main
   ```
8. Go to your local new branch:
   ```
   $ git checkout <new_branch_name>
   ```
9. From your local new branch, merge your two local branches:
   ```
   $ git merge main
   ```
10. FIX ALL MERGING PROBLEMS!
11. Commit all changes to your local new branch:
   ```
   $ git add .
   $ git commit -m "Merged to main branch"
   ```
12. Push to your local new branch branch to your branch on the repo: (this is where you should be committing during 4)
   ```
   $ git push --set-upstream https://github.com/JennicaLeClerc/Synergy_Project2.git <new_branch name>
   ```
13. Submit a pull request on GitHub asking to merge the branch into main.
14. A teammate reviews the code for quality and functionality.
15. The teammate merges the pull request.

### Some ways to not run into merging problems:
- `git status`: displays the state of the working directory and the staging area.
- `git log`: displays committed snapshots. It lets you list the project history, filter it, and search for specific changes.
- `git log --merge`: argument to the git log command will produce a log with a list of commits that conflict between the merging branches.
- `git diff main <your_branch_name>`: compares the two branches and shows the differences bettween the two. 

## Jennica's Rules to the GitHub
- To merge to the main repository you need permission from three others.
  - **MESSAGE THOSE IN THESE GROUPS SO THAT YOU AREN'T WAITING FOR YOUR MERGE** (some of us sleep so it might not be done right away)
  - Testing: someone from testing team must approve of the pull request and then label that pull "Test"
  - Documentation: someone from the testing team must approve of the pull request and then label that pull "Documentation"
  - GitHub: once both Testing and Documentation has approved the pull request, they must approve it and the push to the intended branch
    - **MESSAGE:** those with the pull request if there are any problems 
- To merge you must have all conversations on your code resolved.
- Commits should be logical and/or informative.

## Steps from new branch to merge
1. `$ git init`
2. Create new local branch:
   ```
   $ git checkout -b <new_branch_name>
   ```
   If you want to create a branch from a branch:
   ```
   $ git checkout -b <new_branch_name> <branching_off_of_name>
   ```
3. Create new Branch in GitHub
4. Pull the current GitHub main branch locally:
   ```
   $ git pull https://github.com/RevRelay/Backend.git <new_branch_name>
   ```
5. DO WHAT YOU NEED TO DO! (your current task) Committing as you go... I see you Ryan and John...
6. Checkout the your local branch that you are pushing to (creating that branch if it doesn't exist):
   ```
   $ git checkout <branching_off_of_name>
   ```
   - the head branch is *main*
7. Pull the current GitHub main branch to your local branch that you are merging with:
   ```
   $ git pull
   ```
   - the head branch is *main*
8. Go to your local new branch:
   ```
   $ git checkout <new_branch_name>
   ```
9. Commit all changes to your local new branch:
   ```
   $ git add .
   $ git commit -m "Merged to <name_of_branch> branch"
   ```
10. From your local new branch, merge your two local branches:
   ```
   $ git merge <branching_off_of_name>
   ```
11. FIX ALL MERGING PROBLEMS!
12. Push to your local new branch branch to your branch on the repo: (this is where you should be committing during 4)
   ```
   $ git push --set-upstream https://github.com/RevRelay/Backend.git <new_branch name>
   ```
13. Submit a pull request on GitHub asking to merge the branch into your intendend branch.
14. Three teammates reviews the code for quality and functionality.
  - Testing, Documentation, and Github Masters must approve of this
  - If any other teammate has question or concerns about a pull they may leave a comment, which must be resolved by the user pushing before the merge will be completed.
15. The teammate merges the pull request.

### Some ways to not run into merging problems:
- `git status`: displays the state of the working directory and the staging area.
- `git log`: displays committed snapshots. It lets you list the project history, filter it, and search for specific changes.
- `git log --merge`: argument to the git log command will produce a log with a list of commits that conflict between the merging branches.
- `git diff main <your_branch_name>`: compares the two branches and shows the differences bettween the two. 

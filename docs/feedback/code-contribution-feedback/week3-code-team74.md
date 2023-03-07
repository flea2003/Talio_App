# Code Contributions and Code Reviews

**NEVER FORGET TO MERGE THE DEVELOPMENT BRANCH TO MAIN BEFORE THE MEETING!!!**
I can only review what was added on the main branch, therefore this report will not contain most of your MRs for this week. Please, on Sunday evening (the latest), merge your dev branch into main.

#### Focused Commits

Grade: Insufficient

Feedback: There only 2 commits on the main branch, made by a single person, which is definitely not enough for a whole week. I also looked over the whole repo, but you did not show much activity during this week. Compared to the usual flow of the project, you are behind schedule already, so make sure you start being more invested in this project in order to pass.


#### Isolation

Grade: Sufficient

Feedback: The branches and merge requests seem to be used correctly to isolate individual features during development and are focused on the specific functionality you are implementing, which is good. However, you have **5** MRs ever created, out of which 2 are document-related (for the record, you shouldn't merge documents via MRs, just push those directly to main). This is a very low number compared to the expectations, please step up.


#### Reviewability

Grade: Insufficient

Feedback: The merge requests **need** to have descriptions, in order to make it clear for the other teammates what they are reviewing. Regarding their content, they contain a small amount of commits, with coherent and related changes. However, since the number of MRs is very low, I don't feel like this review is actually a good generalization of your process and I didn't get the idea that the MRs are well constructed overall. Thus, I look forward to see the next week's progress.


#### Code Reviews

Grade: Insufficient

Feedback: You do not have comments. At all. Please write comments at every MR, which should be a discussion about the implemented feature and should reflect an incremental update of the code based on the reviews.


#### Build Server

Grade: Insufficient

Feedback: Your pipelines fail too many times. Please build the project before actually pushing it using Gradle Build (right side of the IDE, you can search it up to see how it's supposed to be run if you don't know that yet)
In addition, you do not have the checkstyle set-up, which was specified to do during the lectures. Please do this until the next meeting, otherwise this section will remain insufficient.


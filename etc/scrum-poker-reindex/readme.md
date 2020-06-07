Lucene Index Timing Tests
=========================

The following steps were used to set up the environment and perform the required Lucene index timing tests for Data Center approval:

* Create database and first Jira Data Center node with docker images    
  https://github.com/fllaca/jira-datacenter-docker

* Install Data Generator for Jira to create required amount of test data    
  https://marketplace.atlassian.com/apps/1210725/data-generator-for-jira

* Generate metadata

> Generated 10 issue statuses in 0:00:00.023    
> Generated 5 standard issue types in 0:00:00.229    
> Generated 1 subtask types in 0:00:00.029    
> Generated 5 issue resolutions in 0:00:00.015    
> Created 50 projects in 0:00:39.284    
> Generated 5 random workflows in 0:00:00.134    
> Assigned 5 random workflows in 0:00:03.359    
> Generated 950 users in 0:01:04.314    
> Generated 50 groups in 0:00:00.418    
> Generated 10 roles in 0:00:00.034    
> Generated 34000 role actors in 0:00:37.794    
> Generated 300 custom fields in 0:00:14.055, Field Managers refresh took 0:00:00.001.    
> Generated 0 screen configs in 0:00:00.000.    
> Generated 1 custom permission scheme in 0:00:09.505

* Generate data

> Created 100 versions in 0:00:00.769    
> - generating versions: 0:00:00.769    
> Created 50 components in 0:00:00.513    
> - generating components: 0:00:00.511    
> Created 1000000 issues in 0:23:59.913    
> - generating issues: 0:11:06.212    
> - generating comments: 0:03:37.821    
> - generating worklogs: 0:03:15.542    
> - generating custom field values: 0:00:15.851    

* Re-Index without Scrum Poker for Jira installed

    * Started  2020/06/06 22:00
    * Finished 2020/06/07 06:43
    * Total    8h 42m 21s

* Take screenshot(s) without app installed

![without app installed](screenshots/2020-06-07 at 10-19-28 reindex without app installed.png?raw=true)
![without app installed](screenshots/2020-06-07 at 10-21-03 reindex without app installed.png?raw=true)

* Install latest version of Scrum Poker for Jira

* Re-Index with Scrum Poker for Jira installed

   * Started   2020/06/07 10:25
   * Finished  2020/06/07 16:32
   * Total     6h 7m 20s

* Take screenshot(s) with app installed

![with app installed](screenshots/2020-06-07 at 18-43-51 reindex with app installed.png?raw=true)
![with app installed](screenshots/2020-06-07 at 18-44-30 reindex with app installed.png?raw=true)

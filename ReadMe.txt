Directions:

    This plugin works currently only with IntelliJ, not PyCharm. Running this project will spawn an instance
of IntelliJ with the plugin installed and running. You will notice there is a window on the right, this window is
pre-loaded with a few example web pages. Clicking start begins the process that runs every 10 seconds that analyzes
the line the cursor is on. In GatherData there is a member boolean variable "HAS_TYPE" that determines whether
to collect type information or not. If HAS_TYPE is set to true, nothing happens, however:
ToDo: if HAS_TYPE is set to false, an object TypeRemover will delete any instances of the types listed in TypeInfo.txt


About The Code:

    - Start with the "GatherData" class "actionPreformed" method.

    - actionPerformed() in GatherData utilizes an "ErrorSystem" object that does the processing of the error system in IntelliJ,
    look in that class for any bugs with processing errors or returning search terms having to do with errors.

    - SearchSystem deals with taking in search terms from ErrorSystem and CaratSystem and changing the URL of the help button
    to the url of the first result on Google of those search terms with "site:stackoverflow.com" appended. This returns the
    first from Stack Overflow that would have shown up on google. It is possible to look at multiple results on google and
    analyze them, that code is commented out in the updateHelpButton() method and can be added in later.

    - ErrorSystem.getTerms(lineNumber, range) now responds to the current line number of the carat and range around the carat
    that is specified to look around. If there are multiple errors with the same contents it will trim it so that only one of
    them is added to the search terms to prevent overloading the search engine.

What's Possible:

We have:
    - Text of Entire Document
    - Carat (Cursor) Position in Text
    - Can take a selection from around carat of line numbers around and on carat.
    - Every Error or Warning in the Error System, in order, line number and error text parsed out to separate variables.
(Info, Note, Generic are also available in the error system, not sure what those are yet)

ReadMe Updated: 2/16


Why?
    The reason we have the TypeRemover is to analyze the effectiveness of queries with type information removed.
We believe this will give us information on whether python's lack of types is detrimental to recommender systems.

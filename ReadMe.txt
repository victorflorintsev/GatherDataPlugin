Directions:

    This plugin works currently only with IntelliJ, not PyCharm. Running this project will spawn an instance
of IntelliJ with the plugin installed and running. You will notice there is a window on the left, pressing
the help button leads you to a web page given by the ErrorSystem object after searching the web for your first
error.

    Currently the help button only works after your project has errors and after "Gather Data" is clicked on the
right click menu when your mouse is over the document editor.

About The Code:

    - Start with the "GatherData" class "actionPreformed" method.
    - actionPerformed() in GatherData utilizes an "ErrorSystem" object that does the processing of the error system in IntelliJ,
    look in that class for any bugs with processing errors or returning search terms having to do with errors.
    - SearchSystem deals with taking in search terms from ErrorSystem and CaratSystem and changing the URL of the help button
    to the url of the first result on Google of those search terms with "site:stackoverflow.com" appended. This returns the
    first from Stack Overflow that would have shown up on google. It is possible to look at multiple results on google and
    analyze them, that code is commented out in the updateHelpButton() method and can be added in later.

What's Possible:

We have:
    - Text of Entire Document
    - Carat (Cursor) Position in Text
    - Can take a selection from around Carat
    - Every Error or Warning in the Error System, in order, line number and error text parsed out to separate variables.
(Info, Note, Generic are also available in the error system, not sure what those are yet)

Updated 12/4
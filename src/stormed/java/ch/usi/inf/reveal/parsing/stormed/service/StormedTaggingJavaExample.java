package stormed.java.ch.usi.inf.reveal.parsing.stormed.service;

import stormed.scala.ch.usi.inf.reveal.parsing.stormed.service.ErrorResponse;
import stormed.scala.ch.usi.inf.reveal.parsing.stormed.service.Response;
import stormed.scala.ch.usi.inf.reveal.parsing.stormed.service.StormedService;
import stormed.scala.ch.usi.inf.reveal.parsing.stormed.service.TaggingResponse;

public class StormedTaggingJavaExample {

	public void test() {
		final String textToTag = 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit\n"+
				"public static void main(int args[])\n"+
				"Proin tincidunt tristique ante, sed lacinia leo fermentum quis.\n"+
				"Fusce in magna eu ante tincidunt euismod nec eu ligula.\n"+
				"List<Integer> someList;";

		
		final String inputWithMissingTagging = 
				"<p>I have an abstract entity:</p>\n"+
			    "<pre><code>public abstract class Entity extends JPanel implements FocusListener</code></pre>\n" +
			    "<p>And I have a TextEntity:</p>\n"+
			    "<pre><code>public class TextEntity extends Entity</code></pre>\n"+
			    "<p>Inside TextEntity's constructor I want to put a JTextArea that will cover the panel:</p>\n"+
			    "<pre>\n"+
			      "<code>\n"+
			      "textArea = new JTextArea();\n" +
			      "textArea.setSize(getWidth(),getHeight());\n" +
			      "add(textArea);\n"+
			      "</code>\n"+
			    "</pre>\n";

		final Response taggedText = StormedService.tag(textToTag, false);
		final Response completeTagging = StormedService.tag(inputWithMissingTagging, true);
		
		System.out.println("###############");
		System.out.println("Example I");
		System.out.println("Plain text without tagging:");
		System.out.println("###############");
		System.out.println(textToTag);
		System.out.println("--------RESPONSE--------");
		printResult(taggedText);
		
		System.out.println();
		System.out.println("###############");
		System.out.println("Example II");
		System.out.println("Text with partial tagging:");
		System.out.println("###############");
		System.out.println(inputWithMissingTagging);
		System.out.println("--------RESPONSE--------");
		printResult(completeTagging);

	}
	
	private static void printResult(final Response response) {
		/*Whenever accessing fields of Scala case classes, 
		 *getters are always in the form variableName.fieldName();
		 */
		switch(response.status()) {
		case "OK":
			final TaggingResponse success = (TaggingResponse) response;
			System.out.println("Status: " + success.status());
			System.out.println("Quota Remaining: " + success.quotaRemaining());
			System.out.println("Parsing Result: ");
			System.out.println("Tagged Text: " + success.result());
			
			break;
		case "ERROR":
			final ErrorResponse error = (ErrorResponse) response;
			System.out.println(error.status() +": " + error.message());
			break;
		}
	}
	
}

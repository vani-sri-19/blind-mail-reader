
package javaapplication2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;

import javax.mail.Message;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Part;
import javax.mail.search.FlagTerm;
import java.util.Locale;
import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
public class JavaApplication2 {
    private boolean textIsHtml = false;
    public static void main(String[] args) throws MessagingException {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
                System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

                Session session = Session.getDefaultInstance(props, null);
                Store store = session.getStore("imaps");

                // IMAP host for gmail. 
                // Replace <username> with the valid username of your Email ID.
                // Replace <password> with a valid password of your Email ID.
//prop.put("mail.imaps.ssl.trust", "*");
                store.connect("imap.gmail.com", "vmuthulakshmi1803@gmail.com", "muthusana1803");

                // IMAP host for yahoo.
                //store.connect("imap.mail.yahoo.com", "<username>", "<password>");

                System.out.println(store);

                Folder inbox = store.getFolder("Inbox");
                inbox.open(Folder.READ_ONLY);
                
                BufferedReader optionReader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Press (U) to get only unread mails OR Press (A) to get all mails:");
                try {
                    char answer = (char) optionReader.read();
                    if(answer=='A' || answer=='a'){
                        showAllMails(inbox);
                    }else if(answer=='U' || answer=='u'){
                        showUnreadMails(inbox);
                    }
                    optionReader.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
                
        } catch (NoSuchProviderException e) {
            System.out.println(e.toString());
            System.exit(1);
        } 
    }
    
    static public void showUnreadMails(Folder inbox){        
        try {
            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message msg[] = inbox.search(ft);
            Multipart ml;
            BodyPart bp; 
            System.out.println("MAILS: "+msg.length);
            for(Message message:msg) {
                try {
                    // set property as Kevin Dictionary
            System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory"); 
                 
            // Register Engine
            Central.registerEngineCentral
                ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
 
            // Create a Synthesizer
            Synthesizer synthesizer =                                         
                Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));     
     
            // Allocate synthesizer
            synthesizer.allocate();        
             
            // Resume Synthesizer
            synthesizer.resume(); 
            ml=(Multipart) message.getContent();
            bp=ml.getBodyPart(1);
     
     //       String con=getText(p);
                    System.out.println("DATE: "+message.getSentDate().toString());
                    System.out.println("FROM: "+message.getFrom()[0].toString());
                    String ab=message.getFrom()[0].toString();
                    synthesizer.speakPlainText(ab,null);
                    System.out.println("SUBJECT: "+message.getSubject().toString());
                   // System.out.println("CONTENT: "+message.getContent().toString());
                    System.out.println("******************************************");
                    String body = message.getDescription();
                    JavaApplication2 e = new JavaApplication2();
                    String subject=message.getSubject().toString();
                    String content=message.getContent().toString();
                    synthesizer.speakPlainText("subject", null);
                    synthesizer.speakPlainText(subject, null);
                    //synthesizer.speakPlainText("content", null);
                   // synthesizer.speakPlainText(content, null);
                    int flag=1;
                    for(int i=0;i<ml.getCount();i++)
                        
                    {
                        Part p = ml.getBodyPart(i);
                        String con = e.getText(p);
                        if(flag==1)
                        {
                        System.out.println(con);
                        synthesizer.speakPlainText(con, null);
                        flag=0;
                        }
                        
                    }
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                 //   System.out.println("No Information");
                }
            }
        } catch (MessagingException e) {
            System.out.println(e.toString());
        }
    }
    

    /**
     * Return the primary text content of the message.
     */
    private String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
          textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    System.out.println("null");
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    System.out.println(s+"text/html");         
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
    static public void showAllMails(Folder inbox){        
        try {
            Message msg[] = inbox.getMessages();
            Multipart ml;
            BodyPart bp; 
            System.out.println("MAILS: "+msg.length);
            for(Message message:msg) {
                try {
                    // set property as Kevin Dictionary
            System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory"); 
                 
            // Register Engine
            Central.registerEngineCentral
                ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
 
            // Create a Synthesizer
            Synthesizer synthesizer =                                         
                Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));     
     
            // Allocate synthesizer
            synthesizer.allocate();        
             
            // Resume Synthesizer
            synthesizer.resume(); 
            ml=(Multipart) message.getContent();
            bp=ml.getBodyPart(1);
     
     //       String con=getText(p);
                    System.out.println("DATE: "+message.getSentDate().toString());
                    System.out.println("FROM: "+message.getFrom()[0].toString());
                    String ab=message.getFrom()[0].toString();
                    synthesizer.speakPlainText(ab,null);
                    System.out.println("SUBJECT: "+message.getSubject().toString());
                    System.out.println("CONTENT: "+message.getContent().toString());
                    System.out.println("******************************************");
                    String body = message.getDescription();
                    JavaApplication2 e = new JavaApplication2();
                    String subject=message.getSubject().toString();
                    String content=message.getContent().toString();
                    synthesizer.speakPlainText("subject", null);
                    synthesizer.speakPlainText(subject, null);
                    synthesizer.speakPlainText("content", null);
                    synthesizer.speakPlainText(content, null);
                    int flag=1;
                    for(int i=0;i<ml.getCount();i++)
                        
                    {
                        Part p = ml.getBodyPart(i);
                        String con = e.getText(p);
                        if(flag==1)
                        {System.out.println(con);
                        synthesizer.speakPlainText(con, null);
                        flag=0;
                        }
                        
                    }
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
                } catch (IOException | IllegalArgumentException | InterruptedException | MessagingException | AudioException | EngineException e) {
                    // TODO Auto-generated catch block
                    //System.out.println("No Information");
                }
            }
        } catch (MessagingException e) 
        {
            System.out.println(e.toString());
        }
    }
}
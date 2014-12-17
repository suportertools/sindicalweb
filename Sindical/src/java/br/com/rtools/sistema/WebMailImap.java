package br.com.rtools.sistema;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

public class WebMailImap {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("imap.gmail.com", "email", "senha");
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] msg = inbox.getMessages();
            int i = 0;
            //Message msg = inbox.getMessage(inbox.getMessageCount());
            for (Message msg1 : msg) {
                try {
                    Address[] in = msg1.getFrom();
                    for (Address address : in) {
                        if (address.toString().contains("mailer-daemon")) {
                            System.out.println("FROM:" + address.toString());
                            try {
                                Object content = msg1.getContent();
                                if (content instanceof String) {
                                    String body = (String) content;
                                    System.out.println("BODY:" + body);
                                } else if (content instanceof Multipart) {
                                    Multipart mp = (Multipart) msg1.getContent();
                                    BodyPart bp = mp.getBodyPart(0);
                                    System.out.println("SENT DATE:" + msg1.getSentDate());
                                    System.out.println("SUBJECT:" + msg1.getSubject());
                                    System.out.println("CONTENT:" + bp.getContent());
                                }
                            } catch (IOException | MessagingException e) {
                                System.out.println("FROM: Erro");
                            }
                        }
                    }
                } catch (MessagingException e) {
                    e.getMessage();
                }
            }
        } catch (MessagingException e) {
            e.getMessage();
        }
    }

}

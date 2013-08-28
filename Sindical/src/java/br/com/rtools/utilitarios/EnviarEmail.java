package br.com.rtools.utilitarios;

import br.com.rtools.pessoa.EnvioEmails;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.EnvioEmailsDB;
import br.com.rtools.pessoa.db.EnvioEmailsDBToplink;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.controleUsuarioJSFBean;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.faces.context.FacesContext;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;

public class EnviarEmail {
    // ENVIAR EMAIL PERSONALIZADO  ------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------
    public static String[] EnviarEmailPersonalizado(Registro sindicato, List<Pessoa> pessoas, String conteudoHTML, List<File> arquivoAnexo, String assunto){
        String[] retorno = new String[2];
        retorno[0] = "";
        retorno[1] = "";
        
        String erroTeste = "";
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/"+ controleUsuarioJSFBean.getCliente()+"/Imagens/LogoCliente.png");
        if (!sindicato.getEmail().isEmpty()){
            Properties props = new Properties();
            Session session = null;
            props.put("mail.host", sindicato.getSmtp());
            // --- GMAIL ---- HOTMAIL ---
            if (sindicato.isEmailAutenticado()){
                props.put("mail.smtp.auth", "true"); // Usa uma conta autenticada
                props.put("mail.smtp.starttls.enable","true");

                Authenticator auth = new myauth(sindicato.getEmail(), sindicato.getSenha());
                session = Session.getInstance(props, auth);
            }else{
                // --- OUTROS ---
                session = Session.getInstance(props, null);
            }

            for (int i = 0; i < pessoas.size(); i ++){
                Juridica jur = (new JuridicaDBToplink()).pesquisaJuridicaPorPessoa(pessoas.get(i).getId());
                if (jur == null)
                    jur = sindicato.getFilial();
                if(!pessoas.get(i).getEmail1().isEmpty()){
                    try{
                        MimeMessage msg = new MimeMessage(session);
                        msg.setFrom(new InternetAddress(sindicato.getEmail()));
                        msg.setRecipient( Message.RecipientType.TO, new InternetAddress(pessoas.get(i).getEmail1()) );
                        
                        //msg.setDescription("Sindicato Name");
                        //msg.setDisposition("Sindicato Name");
                        msg.setHeader("Sindicato Name", "Sindicato Name");
                        
                        msg.setSubject(assunto);
                        msg.setContent(
                                enviarMensagemPersonalizada(
                                    caminho,
                                    "<h2><b>"+sindicato.getFilial().getPessoa().getNome()+"</b></h2><br /><br />"+
                                    //"<p><h3>Esta é uma mensagem automática, por favor NÃO RESPONDA!! <br />" +
                                    "<p><h3>Caso queira entrar em contato envie para: <b>"+sindicato.getFilial().getPessoa().getEmail1()+"</b></h3></p><br /><br />"+
                                    "<h3>A/C</h3> <b> "+jur.getContato()+" </b> <br /><br />",
                                    conteudoHTML,
                                    arquivoAnexo
                                ));
                        transport(msg);
                        //EnviarEmail.gerarHistorico(pessoas.get(i),"","Arquivos anexos");
                        try{
                            Thread.sleep(1000);
                        }catch(Exception e){
                            
                        }
                       // System.out.println("Enviado... "+ pessoas.get(i).getId()+ " - "+ pessoas.get(i).getNome());
                        retorno[0] = " Enviado com Sucesso!";
                        //return retorno;
                    }catch (AddressException e){
                        //throw new IllegalArgumentException("Email de destinatário inválido!");
                        retorno[1] += " "+pessoas.get(i).getEmail1()+" Email de destinatário inválido!";
                        
                       // System.out.println("Erro... "+ pessoas.get(i).getId()+ " - "+pessoas.get(i).getEmail1()+" email de destinatário inválido!");
                        continue;
                    }catch (MessagingException e){
                        //return ""+e;
                        retorno[1] += " "+pessoas.get(i).getNome()+" "+e.getMessage();
                        
                        //System.out.println("Erro... "+ pessoas.get(i).getId()+ " - "+pessoas.get(i).getNome()+" "+e.getMessage());
                        continue;
                    }
                }else{
                    retorno[1] += " "+pessoas.get(i).getNome()+" Empresa não contém Email de contato, Contate o seu Sindicato.";
                    
                    //System.out.println("Erro... "+ pessoas.get(i).getId()+ " - "+pessoas.get(i).getNome()+" empresa não contém Email de contato, Contate o seu Sindicato.");
                    
                }
            }
        }else
            retorno[1] += " "+sindicato.getFilial().getPessoa().getNome()+" Sindicato sem endereço de email para envio!";
        return retorno;
    }
    
    // ENVIAR EMAIL DE SENHA  ------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------
    public synchronized static String EnviarEmail(Registro sindicato,Juridica empresa){
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/"+ controleUsuarioJSFBean.getCliente()+"/Imagens/LogoCliente.png");
        if (!sindicato.getEmail().isEmpty()){
            if(!empresa.getPessoa().getEmail1().isEmpty()){
                if(empresa.getPessoa().getLogin() != null && empresa.getPessoa().getSenha() != null){
                    try{
                        Properties props = new Properties();
                        Session session = null;
                        props.put("mail.host", sindicato.getSmtp());
                        // --- AUTENTICADO ---
                        if (sindicato.isEmailAutenticado()){
                            props.put("mail.smtp.auth", "true"); //Usa uma conta autenticada
                            props.put("mail.smtp.starttls.enable","true");

                            Authenticator auth = new myauth(sindicato.getEmail(), sindicato.getSenha());
                            session = Session.getInstance(props, auth);
                        }else{
                            // --- SEM AUTENTIFICACAO ---
                            session = Session.getInstance(props, null);
                        }


                        MimeMessage msg = new MimeMessage(session);
                        msg.setFrom(new InternetAddress(sindicato.getEmail()));
                        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(empresa.getPessoa().getEmail1()));
                        msg.setSentDate(new Date());
                        msg.setSubject("Envio de Login e Senha");
                        msg.setContent(enviarMensagem(caminho,
                                                      //"Esta é uma mensagem automática, por favor NÃO RESPONDA!! <br>" +
                                                      "Caso queira entrar em contato envie para: "+sindicato.getFilial().getPessoa().getEmail1(),
                                                      empresa,
                                                      sindicato));
                        Transport.send(msg);
                        EnviarEmail.gerarHistorico(empresa,"Login: "+ empresa.getPessoa().getLogin() +", Senha: " + empresa.getPessoa().getSenha(),"Login e Senha");
                        return "Enviado com Sucesso. Confira email cadastrado!";
                    }catch (AddressException e){
                        //throw new IllegalArgumentException("Email de destinatário inválido!");
                        return "Email de destinatário inválido!";
                    }catch (MessagingException e){
                        return ""+e;
                    }
                }else{
                    return "Empresa não contém Login e Senha, solicite a sua entrando em contado com o seu Sindicato.";
                }
            }else{
                return "Empresa não contém Email de contato, Contate o seu Sindicato.";
            }
        }else{
            return "Sindicato sem endereço de email para envio!";
        }
    }

    public static String EnviarEmailAutomatico(Registro sindicato,List<Juridica>empresas){
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/"+ controleUsuarioJSFBean.getCliente()+"/Imagens/LogoCliente.png");
        String msg = "";
        if (!sindicato.getEmail().isEmpty()){
            Properties props = new Properties();
            Session session = null;
            props.put("mail.host", sindicato.getSmtp());
            // --- GMAIL ---- HOTMAIL ---
            if (sindicato.isEmailAutenticado()){
                props.put("mail.smtp.auth", "true"); //Usa uma conta autenticada
                props.put("mail.smtp.starttls.enable","true");

                Authenticator auth = new myauth(sindicato.getEmail(), sindicato.getSenha());
                session = Session.getInstance(props, auth);
            }else{
                // --- OUTROS ---
                session = Session.getInstance(props, null);
            }

            for (int i = 0; i < empresas.size(); i ++){
                if(!empresas.get(i).getPessoa().getEmail1().isEmpty()){
                    if(empresas.get(i).getPessoa().getLogin() != null && empresas.get(i).getPessoa().getSenha() != null){
                        try{

                            MimeMessage mmsg = new MimeMessage(session);
                            mmsg.setFrom(new InternetAddress(sindicato.getEmail()));
                            mmsg.setRecipient(Message.RecipientType.TO, new InternetAddress(empresas.get(i).getPessoa().getEmail1()) );
                            mmsg.setSubject("Envio de Login e Senha");
                            mmsg.setContent(enviarMensagem(caminho,
                                                          //"Esta é uma mensagem automática, por favor NÃO RESPONDA!! <br>" +
                                                          "Caso queira entrar em contato envie para: "+sindicato.getFilial().getPessoa().getEmail1(),
                                                          empresas.get(i),
                                                          sindicato));
                            transport(mmsg);
                            EnviarEmail.gerarHistorico(empresas.get(i),"Login: "+ empresas.get(i).getPessoa().getLogin() +", Senha: " + empresas.get(i).getPessoa().getSenha(),"Login e Senha");
                            if (msg.isEmpty())
                                msg = "Enviado com Sucesso!";
                        }catch (AddressException e){
                            //throw new IllegalArgumentException("Email de destinatário inválido!");
                            msg+= " Email de destinatário inválido! para: "+empresas.get(i).getPessoa().getEmail1()+" ";
                            continue;
                        }catch (MessagingException e){
                            msg+= " "+e;
                            continue;
                        }
                    }else{
                        //return "Empresa não contém Login e Senha, solicite a sua entrando em contado com o seu Sindicato.";
                    }
                }else{
                    msg += " Empresa "+empresas.get(i).getPessoa().getNome() +" não contém Email de contato, Contate o seu Sindicato.";
                }
            }
        }
            //return "Sindicato sem endereço de email para envio!";
        return msg;
    }

    public synchronized static void transport( MimeMessage msg) throws MessagingException{
        Transport.send(msg);
    }



        private static Multipart enviarMensagem(String logo,
                                                String conteudo,
                                                Juridica empresa,
                                                Registro sindicato)throws MessagingException{
            MimeMultipart multipart = new MimeMultipart("related");

            String html = "";
            html += "<html><body style='background-color: white'>";
            //html += "<img src='"+logo+"'></img>";
            html += "<hr/>";
            html += "<h2><b>"+sindicato.getFilial().getPessoa().getNome()+"</b></h2><br /><br />";
            html += "<p><h3>" + conteudo + "</h3></p><br /><br />";
            html += "<p> Olá "+ empresa.getPessoa().getNome() +" envio de Login e Senha para acesso ao Sindical Web</p>";
            html += "<p> Impressão de guias de contribuição acesse: </p>";
            html += "<p><b>"+ sindicato.getFilial().getPessoa().getSite() +" -> Menu Serviços -> Acesso do Contribuinte.</b></p>";
            html += "<br />";
            html += "<p><b>Login:</b> <i>"+empresa.getPessoa().getLogin()+"</i></p>";
            html += "<p><b>Senha:</b> <i>"+empresa.getPessoa().getSenha()+"</i></p><br />";
            html += "<p>Para mais Informacões: "+ sindicato.getFilial().getPessoa().getTelefone1() + " / "+ sindicato.getFilial().getPessoa().getTelefone2()  +"</p>";
            html += "<p>Ou acesse: "+ sindicato.getFilial().getPessoa().getSite() +" </p>";
            html += "<hr/><br />";
            html += sindicato.getFilial().getPessoa().getNome()+"<br />";
            html += "Emails alternativos: "+sindicato.getFilial().getPessoa().getEmail1() +" / "+sindicato.getFilial().getPessoa().getEmail2();
            html += "<br /><br />";
            html += "<h3>A/C</h3> <b> "+empresa.getContato()+" </b></body></html>";
            
            BodyPart mainPart = new MimeBodyPart();
            mainPart.setContent(html, "text/html; charset=utf-8"); //Adiciona conteúdo HTML
            multipart.addBodyPart(mainPart);

//            BodyPart imagePart = new MimeBodyPart();
//            imagePart.setHeader("Content-ID", "<img>"); //Adiciona a imagem do logo, seta o id como image
//            multipart.addBodyPart(imagePart);
        return multipart;
    }
        
        private static Multipart enviarMensagemPersonalizada(String logo,
                                                String conteudo,
                                                String conteudoHTML,
                                                List<File> arquivoAnexo)throws MessagingException{
            MimeMultipart multipart = new MimeMultipart("related");


            BodyPart mainPart = new MimeBodyPart();
            mainPart.setContent(conteudo + conteudoHTML, "text/html; charset=utf-8"); //Adiciona conteúdo HTML
            multipart.addBodyPart(mainPart);

            for (int i = 0; i < arquivoAnexo.size(); i++){
                // PARTE QUE IRA CHAMAR O CAMINHO DO ANEXO ------
                BodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<img>"); //Adiciona a imagem do logo, seta o id como image
                DataSource imgFds = new FileDataSource(arquivoAnexo.get(i));
                imagePart.setDataHandler( new DataHandler(imgFds) );
                imagePart.setFileName(arquivoAnexo.get(i).getName());
                multipart.addBodyPart(imagePart);            
            }
        return multipart;
    }

    // DAQUI PARA BAIXO O ENVIO COM ANEXO ------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------
    public synchronized static String EnviarEmailComAnexo(Registro sindicato,Juridica empresa,String nomePdf, File arquivoAnexo){
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/"+ controleUsuarioJSFBean.getCliente()+"/Imagens/LogoCliente.png");
        if (!sindicato.getEmail().isEmpty()){
            if(!empresa.getPessoa().getEmail1().isEmpty()){
                try{
                    Properties props = new Properties();
                    Session session = null;
                    props.put("mail.host", sindicato.getSmtp());
                    // --- GMAIL ---- HOTMAIL ---
                    if (sindicato.isEmailAutenticado()){
                        props.put("mail.smtp.auth", "true"); //Usa uma conta autenticada
                        props.put("mail.smtp.starttls.enable","true");

                        Authenticator auth = new myauth(sindicato.getEmail(), sindicato.getSenha());
                        session = Session.getInstance(props, auth);
                    }else{
                        // --- OUTROS ---
                        session = Session.getInstance(props, null);
                    }

                    MimeMessage msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(sindicato.getEmail()));
                    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(empresa.getPessoa().getEmail1()) );
                    msg.setSubject("Envio de Arquivo");
                    msg.setContent(enviarMensagemComAnexo(caminho,
                                                          //"Esta é uma mensagem automática, por favor NÃO RESPONDA!! <br>" +
                                                          "Caso queira entrar em contato envie para: "+sindicato.getFilial().getPessoa().getEmail1(),
                                                          empresa,
                                                          sindicato,
                                                          nomePdf,
                                                          arquivoAnexo));
                    Transport.send(msg);
                    EnviarEmail.gerarHistorico(empresa, nomePdf, "Envio de guias");
                    return "Enviado com Sucesso!";
                }catch (AddressException e){
                    //throw new IllegalArgumentException("Email de destinatário inválido!");
                    return "Email de destinatário inválido!";
                }catch (MessagingException e){
                    return ""+e;
                }
            }else{
                return "Empresa não contém Email de contato!";
            }
        }else{
            return "Sindicato sem endereço de email para envio!";
        }
    }

    private static Multipart enviarMensagemComAnexo(String logo,
                                                    String conteudo,
                                                    Juridica empresa,
                                                    Registro sindicato,
                                                    String nomePdf,
                                                    File arquivoAnexo)throws MessagingException{
        MimeMultipart multipart = new MimeMultipart("related");

        String html = "";
            html += "<html><body style='background-color: white'>";
            html += "<img src='"+logo+"'></img>";
            html += "<hr/>";
            html += "<h2><b>"+sindicato.getFilial().getPessoa().getNome()+"</b></h2><br /><br />";
            html += "<p><h3>" + conteudo + "</h3></p><br /><br />";
            html += "<br />";
            html += "<br />";
            html += "<p>Para mais Informacões: "+ sindicato.getFilial().getPessoa().getTelefone1() + " / "+ sindicato.getFilial().getPessoa().getTelefone2()  +"</p>";
            html += "<p>Ou acesse: "+ sindicato.getFilial().getPessoa().getSite() +" </p>";
            html += "<hr/><br />";
            html += sindicato.getFilial().getPessoa().getNome()+"<br/>";
            html += "Emails alternativos: "+sindicato.getFilial().getPessoa().getEmail1() +" / "+sindicato.getFilial().getPessoa().getEmail2();
            html += "<br /><br />";
            html += "<h3>A/C</h3> <b> "+empresa.getContato()+" </b></body></html>";

            BodyPart mainPart = new MimeBodyPart();
            mainPart.setContent(html, "text/html; charset=utf-8"); //Adiciona conteúdo HTML
            multipart.addBodyPart(mainPart);

        // PARTE QUE IRA CHAMAR O CAMINHO DO ANEXO ------
            BodyPart imagePart = new MimeBodyPart();
            //DataSource imgFds  = new FileDataSource("C:/rel.pdf");
            DataSource imgFds  = new FileDataSource(arquivoAnexo);
            imagePart.setDataHandler( new DataHandler(imgFds) );
            imagePart.setFileName(nomePdf);
            imagePart.setHeader("Content-ID", "<img>"); //Adiciona a imagem do logo, seta o id como image
            multipart.addBodyPart(imagePart);
        return multipart;
    }

    public static class myauth extends Authenticator {
        String UserName = null;
        String Password = null;

        public myauth(String UserName,String Password) {
            this.UserName = UserName;
            this.Password = Password;
        }
                @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(UserName, Password);
        }
    }

    public static void gerarHistorico(Juridica empresa,String historico,String tipo){
        EnvioEmails envioEmails = null;
        EnvioEmailsDB envioEmailsDB = new EnvioEmailsDBToplink();
                                envioEmails = new EnvioEmails(
                                -1,
                                empresa.getPessoa(),
                                empresa.getPessoa().getEmail1(),
                                historico,
                                tipo
                                );
        envioEmailsDB.insert(envioEmails);

    }
}


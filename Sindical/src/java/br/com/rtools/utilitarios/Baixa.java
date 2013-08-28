/*

    public boolean darBaixa(List<Movimento> movimentos, List pagamentos, Usuario usuario){
        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
        RotinaDB dbRot = new RotinaDBToplink();
        FilialDB filDB = new FilialDBToplink();
        PessoaDB pesDB = new PessoaDBToplink();
        TipoServicoDB tipoServicoDB = new TipoServicoDBToplink();
        FTipoDocumentoDB tipoDocDB = new FTipoDocumentoDBToplink();
        ServicosDB servDB = new ServicosDBToplink();
        Plano5DB pl5DB = new Plano5DBToplink();
        LoteBaixaDB baixaDB = new LoteBaixaDBToplink();

        int i = 0;
        Movimento[] baixar = new Movimento[movimentos.size() + pagamentos.size()];
        float total = 0;
        while(i < movimentos.size()){
            total = Moeda.somaValores(total, movimentos.get(i).getValor());
            i++;
        }

        i = 0;
        int j = 0;
        Lote lote = new Lote(-1, DataHoje.data(), movimentos.size(), total, dbRot.pesquisaCodigo(4) ,"R", movimentos.get(0).getLote().getCompetencia());

        financeiroDB.insert(lote);
        LoteBaixa loteB = new LoteBaixa(-1,
                usuario,
                null,
                null,
                DataHoje.converte(movimentos.get(i).getLote().getCompetencia()));
        financeiroDB.insert(loteB);

        while (j < movimentos.size()){
            baixar[j] = new Movimento(
                        -1,
                        lote,
                        movimentos.get(j).getFTipoDocumento(),
                        movimentos.get(j).getServicos().getPlano5Credito(),
                        null,
                        movimentos.get(j).getFilial(),
                        movimentos.get(j).getPessoa(),
                        movimentos.get(j).getVencimento(),
                        movimentos.get(j).getNumero(),
                        movimentos.get(j).getValor(),
                        movimentos.get(j).getAcordo(),
                        movimentos.get(j).getNomePessoa(),
                        "C",
                        baixaDB.pesquisaCodigo(loteB.getId()),
                        movimentos.get(j).getServicos(),
                        tipoServicoDB.pesquisaCodigo(1),
                        movimentos.get(j).getVencimento().substring(4, movimentos.get(j).getVencimento().length())
                    );
            j++;
        }

        while(i < pagamentos.size()){
            String referencia = movimentos.get(0).getVencimento().substring(4, movimentos.get(0).getVencimento().length());
            baixar[j] = new Movimento(
                        -1,
                        lote,
                        tipoDocDB.pesquisaCodigo(Integer.parseInt(((GenericaQuery) (pagamentos.get(i))).getArgumento7())),
                        pl5DB.pesquisaCodigo(Integer.parseInt(((GenericaQuery) (pagamentos.get(i))).getArgumento6())),
                        null,
                        movimentos.get(0).getFilial(),
                        movimentos.get(0).getPessoa(),
                        ((GenericaQuery) (pagamentos.get(i))).getArgumento1(),
                        ((GenericaQuery) (pagamentos.get(i))).getArgumento6(),
                        Moeda.substituiVirgulaFloat(((GenericaQuery) (pagamentos.get(i))).getArgumento5()),
                        movimentos.get(0).getAcordo(),
                        movimentos.get(0).getNomePessoa(),
                        "D",
                        baixaDB.pesquisaCodigo(loteB.getId()),
                        movimentos.get(0).getServicos(),
                        tipoServicoDB.pesquisaCodigo(1),
                        referencia
                    );
            i++;
            j++;
        }
        baixar(baixar);
        return false;
    }

    public boolean baixar(Movimento[] movimentos){
        boolean result = false;
        FinanceiroDB db = new FinanceiroDBToplink();
        int i = 0;
        while (i < movimentos.length){
            result = db.insert(movimentos[i]);
            if (result == false)
                return result;
            i++;
        }
        return true;
    }

    public void gerarMovimentoComplementoValor(List<ComplementoValor> complemento, String[] totais){
       FinanceiroDB finDB = new FinanceiroDBToplink();
       ServicosDB dbServ = new ServicosDBToplink();
       FilialDB filDB = new FilialDBToplink();
       PessoaDB pesDB = new PessoaDBToplink();
       TipoServicoDB tipoServicoDB = new TipoServicoDBToplink();
       int i = 0;
       i = 0;
       while (i < complemento.size()){
           finDB.insert(complemento.get(i));
           i++;
       }

       i = 0;
       Movimento[] baixar = new Movimento[5];
       int idServ = 0;
       while(i < 5){
           switch(i){
               case 0:
                   idServ = 7;
               break;
               case 1:
                   idServ = 6;
               break;
               case 2:
                   idServ = 11;
               break;
               case 3:
                   idServ = 10;
               break;
               case 4:
                   idServ = 8;
               break;
           }

           baixar[i] = new Movimento(
                       -1,
                       complemento.get(0).getMovimento().getLote(),
                       null,
                       dbServ.pesquisaCodigo(idServ).getPlano5Debito(),
                       null,
                       filDB.pesquisaCodigo(1),
                       pesDB.pesquisaCodigo(0),
                       complemento.get(0).getMovimento().getVencimento(),
                       "",
                       Float.parseFloat(totais[i]),
                       -1,
                       "",
                       "D",
                       complemento.get(0).getMovimento().getLoteBaixa(),
                       dbServ.pesquisaCodigo(idServ),
                       tipoServicoDB.pesquisaCodigo(1),
                       complemento.get(0).getMovimento().getVencimento().substring(4, complemento.get(0).getMovimento().getVencimento().length())
          );
          i++;
      }
    }

    public float[] calculoComplementoValor(float[] acrescimo, float desconto, BigDecimal valorReal){
        int i = 0;
        float pD = 0; // percentual desconto
        float dpA = 0; // desconto por acrescimo
        float tmp = 0;
        float somaVetor = somaValoresVetor(acrescimo);
        if (desconto == 0){
            return acrescimo;
        }

        if ((desconto == 0)){
            return new float[] {0, 0, 0};
        }else if (desconto <  somaVetor){
            pD = Moeda.divisaoValores(valorReal.floatValue(), desconto);
            while (i < acrescimo.length){
                dpA = Moeda.multiplicarValores(pD, acrescimo[i]);
                tmp = Moeda.subtracaoValores(acrescimo[i], dpA);
                acrescimo[i] = tmp;
                i++;
            }
        }else{
            acrescimo = new float[] {
                Moeda.subtracaoValores(valorReal.floatValue(), Moeda.subtracaoValores(desconto, somaVetor))
            };
        }
        return acrescimo;
    }

    public float somaValoresVetor(float[] vetor){
        float resultado = 0;
        int i = 0;
        while (i < vetor.length){
            resultado = Moeda.somaValores(resultado, vetor[i]);
            i++;
        }
        return resultado;
    }*/
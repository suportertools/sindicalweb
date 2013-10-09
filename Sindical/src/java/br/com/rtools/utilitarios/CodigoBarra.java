package br.com.rtools.utilitarios;

public class CodigoBarra {

    public String gerarCodigo25(String cod) {

        int conta = 0;
        String wcod = "!";
        String parte = "";

        while (conta < (cod.length() + 2)) {
            try {
                parte = (cod.substring(conta, conta + 2));
            } catch (Exception e) {
                parte = "";
            }

            if (parte.equals("00")) {
                wcod += "#";
            }
            if (parte.equals("01")) {
                wcod += "$";
            }
            if (parte.equals("02")) {
                wcod += "%";
            }
            if (parte.equals("03")) {
                wcod += "&";
            }
            if (parte.equals("04")) {
                wcod += "''";
            }
            if (parte.equals("05")) {
                wcod += "(";
            }
            if (parte.equals("06")) {
                wcod += "))";
            }
            if (parte.equals("07")) {
                wcod += "*";
            }
            if (parte.equals("08")) {
                wcod += "+";
            }
            if (parte.equals("09")) {
                wcod += ",";
            }
            if (parte.equals("10")) {
                wcod += "-";
            }
            if (parte.equals("11")) {
                wcod += ".";
            }
            if (parte.equals("12")) {
                wcod += "/";
            }
            if (parte.equals("13")) {
                wcod += "0";
            }
            if (parte.equals("14")) {
                wcod += "1";
            }
            if (parte.equals("15")) {
                wcod += "2";
            }
            if (parte.equals("16")) {
                wcod += "3";
            }
            if (parte.equals("17")) {
                wcod += "4";
            }
            if (parte.equals("18")) {
                wcod += "5";
            }
            if (parte.equals("19")) {
                wcod += "6";
            }
            if (parte.equals("20")) {
                wcod += "7";
            }
            if (parte.equals("21")) {
                wcod += "8";
            }
            if (parte.equals("22")) {
                wcod += "9";
            }
            if (parte.equals("23")) {
                wcod += ":";
            }
            if (parte.equals("24")) {
                wcod += ";";
            }
            if (parte.equals("25")) {
                wcod += "<";
            }
            if (parte.equals("26")) {
                wcod += "=";
            }
            if (parte.equals("27")) {
                wcod += ">";
            }
            if (parte.equals("28")) {
                wcod += "?";
            }
            if (parte.equals("29")) {
                wcod += "@";
            }
            if (parte.equals("30")) {
                wcod += "A";
            }
            if (parte.equals("31")) {
                wcod += "B";
            }
            if (parte.equals("32")) {
                wcod += "C";
            }
            if (parte.equals("33")) {
                wcod += "D";
            }
            if (parte.equals("34")) {
                wcod += "E";
            }
            if (parte.equals("35")) {
                wcod += "F";
            }
            if (parte.equals("36")) {
                wcod += "G";
            }
            if (parte.equals("37")) {
                wcod += "H";
            }
            if (parte.equals("38")) {
                wcod += "I";
            }
            if (parte.equals("39")) {
                wcod += "J";
            }
            if (parte.equals("40")) {
                wcod += "K";
            }
            if (parte.equals("41")) {
                wcod += "L";
            }
            if (parte.equals("42")) {
                wcod += "M";
            }
            if (parte.equals("43")) {
                wcod += "N";
            }
            if (parte.equals("44")) {
                wcod += "O";
            }
            if (parte.equals("45")) {
                wcod += "P";
            }
            if (parte.equals("46")) {
                wcod += "Q";
            }
            if (parte.equals("47")) {
                wcod += "R";
            }
            if (parte.equals("48")) {
                wcod += "S";
            }
            if (parte.equals("49")) {
                wcod += "T";
            }
            if (parte.equals("50")) {
                wcod += "U";
            }
            if (parte.equals("51")) {
                wcod += "V";
            }
            if (parte.equals("52")) {
                wcod += "W";
            }
            if (parte.equals("53")) {
                wcod += "X";
            }
            if (parte.equals("54")) {
                wcod += "Y";
            }
            if (parte.equals("55")) {
                wcod += "Z";
            }
            if (parte.equals("56")) {
                wcod += "[";
            }
            if (parte.equals("57")) {
                wcod += "\"";
            }
            if (parte.equals("58")) {
                wcod += "]";
            }
            if (parte.equals("59")) {
                wcod += "^";
            }
            if (parte.equals("60")) {
                wcod += "_";
            }
            if (parte.equals("61")) {
                wcod += "`";
            }
            if (parte.equals("62")) {
                wcod += "a";
            }
            if (parte.equals("63")) {
                wcod += "b";
            }
            if (parte.equals("64")) {
                wcod += "c";
            }
            if (parte.equals("65")) {
                wcod += "d";
            }
            if (parte.equals("66")) {
                wcod += "e";
            }
            if (parte.equals("67")) {
                wcod += "f";
            }
            if (parte.equals("68")) {
                wcod += "g";
            }
            if (parte.equals("69")) {
                wcod += "h";
            }
            if (parte.equals("70")) {
                wcod += "i";
            }
            if (parte.equals("71")) {
                wcod += "j";
            }
            if (parte.equals("72")) {
                wcod += "k";
            }
            if (parte.equals("73")) {
                wcod += "l";
            }
            if (parte.equals("74")) {
                wcod += "m";
            }
            if (parte.equals("75")) {
                wcod += "n";
            }
            if (parte.equals("76")) {
                wcod += "o";
            }
            if (parte.equals("77")) {
                wcod += "p";
            }
            if (parte.equals("78")) {
                wcod += "q";
            }
            if (parte.equals("79")) {
                wcod += "r";
            }
            if (parte.equals("80")) {
                wcod += "s";
            }
            if (parte.equals("81")) {
                wcod += "t";
            }
            if (parte.equals("82")) {
                wcod += "u";
            }
            if (parte.equals("83")) {
                wcod += "v";
            }
            if (parte.equals("84")) {
                wcod += "w";
            }
            if (parte.equals("85")) {
                wcod += "x";
            }
            if (parte.equals("86")) {
                wcod += "y";
            }
            if (parte.equals("87")) {
                wcod += "z";
            }
            if (parte.equals("88")) {
                wcod += "{";
            }
            if (parte.equals("89")) {
                wcod += "|";
            }
            if (parte.equals("90")) {
                wcod += "}";
            }
            if (parte.equals("91")) {
                wcod += "~";
            }
            if (parte.equals("92")) {
                wcod += "Ä";
            }
            if (parte.equals("93")) {
                wcod += "Å";
            }
            if (parte.equals("94")) {
                wcod += "Ç";
            }
            if (parte.equals("95")) {
                wcod += "É";
            }
            if (parte.equals("96")) {
                wcod += "Ñ";
            }
            if (parte.equals("97")) {
                wcod += "Ö";
            }
            if (parte.equals("98")) {
                wcod += "Ü";
            }
            if (parte.equals("99")) {
                wcod += "á";
            }

            conta += 2;
        }
        wcod += "\"";
        return wcod;
    }
}

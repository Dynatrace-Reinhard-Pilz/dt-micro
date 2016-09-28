package com.dynatrace.microservices.remoting.documents;

public class Lorem {

	private static final String[] LINES = {
		"Lorem ipsum dolor sit amet, no eos vidit novum graeci, sed epicurei platonem in. Sea graece omnesque mandamus te, no nostro cetero usu, cum in perfecto consequat. Ius at iusto ocurreret, vel cu quot adipiscing, primis persequeris te ius. Eu mea noster virtute. Nominavi recteque id his. Omnes urbanitas incorrupte te vel, libris facete evertitur id eos, at antiopam efficiendi est.",
		"Pri ne graece noluisse ocurreret, eu ius ipsum constituto, no nam veri aliquam inimicus. Id honestatis scripserit liberavisse mea. Oratio appellantur cu duo. Ne audiam incorrupte nam. Vis idque dicunt ei, ipsum solet expetenda per cu.",
		"Ei mea cibo vivendum. Usu melius percipit necessitatibus an, ei sea modo impetus vivendo, nec ex facer adipiscing ullamcorper. Propriae interpretaris ut eos. Quo iriure appellantur ne, ei vix erant facilisis, inani malorum blandit ex cum.",
		"Veniam discere inimicus his no. Nec etiam intellegam ex, affert corpora pertinax quo an. Ea iisque vocibus volutpat sed, te nec dolore epicurei. Ut zril possim pri. Pro probo adolescens et.",
		"At eum elit consulatu, sanctus vivendo ei nam, veniam feugiat ad mel. Sed an accusata concludaturque, est ea stet populo epicuri, debitis copiosae duo ne. Ei numquam mnesarchum vituperatoribus ius, at pri ullum noster. Vix virtute evertitur no, solum molestiae quo cu, congue hendrerit reformidans ne sit."
	};
	
	private static final String TEXT = init();
	
	private static String init() {
		StringBuilder sb = new StringBuilder();
		for (String line : LINES) {
			sb.append(line).append("\r\n");
		}
		return sb.toString();
	}
	
	public static String ipsum() {
		return TEXT;
	}
	
	public static String ipsum(final int len) {
		StringBuilder sb = new StringBuilder();
		int curLen = 0;
		while (curLen < len) {
			curLen = curLen + TEXT.length();
			sb.append(TEXT);
		}
		String all = sb.toString();
		if (all.length() == len) {
			return all;
		}
		return all.substring(0, len);
	}
}

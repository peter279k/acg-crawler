package email;

import java.sql.Connection;
import java.util.ArrayList;

import database.DbConnection;
import parser.HtmlParser;

public class EmailTemplate {
	public String generate() {
		ArrayList<String> contents = this.getAnimeNews();
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("<!DOCTYPE html PUBLIC \'-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\'>");
		strBuilder.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
		strBuilder.append("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><title>動漫電子報</title>");
		strBuilder.append("<style type='text/css'>");
		strBuilder.append("a:link {color: blue;}a:link {color: red;}a:active {color: orange;}");
		strBuilder.append("body{padding-top:0!important;padding-bottom:0!important;padding-top:0!important;padding-bottom:0!important;margin:0!important;width:100%!important;-webkit-text-size-adjust:100%!important;-ms-text-size-adjust:100%!important;-webkit-font-smoothing:antialiased!important}");
		strBuilder.append(".tableContent img{border:0!important;display:block!important;outline:none!important}a{color:#382f2e}p,h1{color:#382f2e;margin:0}p{text-align:left;color:#999;font-size:14px;font-weight:normal;line-height:19px}a.link1{color:#382f2e}a.link2{font-size:16px;text-decoration:none;color:#fff}h2{text-align:left;color:#222;font-size:19px;font-weight:normal}div,p,ul,h1{margin:0}.bgBody{background:#fff}.bgItem{background:#fff}");
		strBuilder.append("@media only screen and (max-width:480px){table[class='MainContainer'],td[class='cell']{width:100%!important;height:auto!important}td[class='specbundle']{width:100%!important;float:left!important;font-size:13px!important;line-height:17px!important;display:block!important;padding-bottom:15px!important}");
		strBuilder.append("td[class='spechide']{display:none!important}img[class='banner']{width:100%!important;height:auto!important}td[class='left_pad']{padding-left:15px!important;padding-right:15px!important}}");
		strBuilder.append("@media only screen and (max-width:540px){table[class='MainContainer'],td[class='cell']{width:100%!important;height:auto!important}td[class='specbundle']{width:100%!important;float:left!important;font-size:13px!important;line-height:17px!important;display:block!important;padding-bottom:15px!important}td[class='spechide']{display:none!important}img[class='banner']{width:100%!important;height:auto!important}.font{font-size:18px!important;line-height:22px!important}.font1{font-size:18px!important;line-height:22px!important}}</style>");
		strBuilder.append("<script type='colorScheme' class='swatch active'>{'name':'Default','bgBody':'ffffff','link':'382F2E','color':'999999','bgItem':'ffffff','title':'222222'}</script></head>");
		strBuilder.append("<body paddingwidth='0' paddingheight='0' style='padding-top: 0; padding-bottom: 0; padding-top: 0; padding-bottom: 0; background-repeat: repeat; width: 100% !important; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; -webkit-font-smoothing: antialiased;' offset='0' toppadding='0' leftpadding='0'><table bgcolor='#ffffff' width='100%' border='0' cellspacing='0' cellpadding='0' class='tableContent' align='center' style='font-family:Helvetica, Arial,serif;'><tbody><tr><td><table width='600' border='0' cellspacing='0' cellpadding='0' align='center' bgcolor='#ffffff' class='MainContainer'><tbody><tr><td><table width='100%' border='0' cellspacing='0' cellpadding='0'><tbody><tr><td valign='top' width='40'>&nbsp;</td><td><table width='100%' border='0' cellspacing='0' cellpadding='0'><tbody><!-- =============================== Header ====================================== --><tr><td height='75' class='spechide'></td><!-- =============================== Body ====================================== --></tr><tr><td class='movableContentContainer ' valign='top'><div class='movableContent' style='border: 0px; padding-top: 0px; position: relative;'><table width='100%' border='0' cellspacing='0' cellpadding='0'><tbody><tr><td height='35'></td></tr><tr><td><table width='100%' border='0' cellspacing='0' cellpadding='0'><tbody><tr><td valign='top' align='center' class='specbundle'><div class='contentEditableContainer contentTextEditable'><div class='contentEditable'><p style='text-align:center;margin:0;font-family:Georgia,Time,sans-serif;font-size:26px;color:#222222;'><span class='specbundle2'><span class='font1'>動漫電子報</span></span></p></div></div></td><td valign='top' class='specbundle'><div class='contentEditableContainer contentTextEditable'><div class='contentEditable'><p style='text-align:center;margin:0;font-family:Georgia,Time,sans-serif;font-size:26px;color:#1A54BA;'><span class='font'>provided by <a target='_blank' href='https://github.com/peter279k'>peter279k</a></span></p></div></div></td></tr></tbody></table></td></tr></tbody></table></div><div class='movableContent' style='border: 0px; padding-top: 0px; position: relative;'><table width='100%' border='0' cellspacing='0' cellpadding='0' align='center'><tr><td height='55'></td></tr><tr><td align='left'><div class='contentEditableContainer contentTextEditable'><div class='contentEditable' align='center'><h2>Hello,</h2></div></div></td></tr><tr><td height='15'></td></tr>");
		if(contents.size() == 0) {
			strBuilder.append("<tr><td align='left'>");
			strBuilder.append("<div class='contentEditableContainer contentTextEditable'>");
			strBuilder.append("<div class='contentEditable' align='center'>");
			strBuilder.append("<h2>今日未有動漫情報！</h2>");
			strBuilder.append("</div></div></td></tr>");
		} else {
			for(int index=0;index<contents.size();index+=4) {
				String id = contents.get(index);
				String title = contents.get(index+1);
				String link = contents.get(index+2);
				String dat = contents.get(index+3);

				strBuilder.append("<tr><td align='left'>");
				strBuilder.append("<div class='contentEditableContainer contentTextEditable'>");
				strBuilder.append("<div class='contentEditable' align='center'>");
				strBuilder.append("<h2>" + id + " (" + dat + ")" + "</h2>");
				strBuilder.append("<h2><a href='" + link + "'>" + title + "</a></h2>");
				strBuilder.append("</div></div></td></tr>");
			}
			strBuilder.append("<tr><td height='55'></td></tr><tr><td align='center'><table><tr><td align='center' bgcolor='#1A54BA' style='background:#1A54BA; padding:15px 18px;-webkit-border-radius: 4px; -moz-border-radius: 4px; border-radius: 4px;'><div class='contentEditableContainer contentTextEditable'><div class='contentEditable' align='center'><a target='_blank' href='https://peter279k.com.tw' class='link2' style='color:#ffffff;'>退訂</a></div></div></td></tr></table></td></tr><tr><td height='20'></td></tr></table></div><div class='movableContent' style='border: 0px; padding-top: 0px; position: relative;'><table width='100%' border='0' cellspacing='0' cellpadding='0'><tbody><tr><td height='65'></tr><tr><td style='border-bottom:1px solid #DDDDDD;'></td></tr><tr><td height='25'></td></tr><tr><td><table width='100%' border='0' cellspacing='0' cellpadding='0'><tbody><tr><td valign='top' width='30' class='specbundle'>&nbsp;</td><td valign='top' class='specbundle'><table width='100%' border='0' cellspacing='0' cellpadding='0'></table></td></tr><tr><td valign='top' width='200' class='specbundle'>產生日期：" + HtmlParser.getTodayDat() + "</td></tr></tbody></table></td></tr><tr><td height='88'></td></tr></tbody></table></div><!-- =============================== footer ====================================== --></td></tr></tbody></table></td><td valign='top' width='40'>&nbsp;</td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></body></html>");
		}
		return strBuilder.toString();
	}
	
	private ArrayList<String> getAnimeNews() {
		DbConnection dbConn = new DbConnection();
		Connection conn = dbConn.iniConnection();

		return dbConn.selectValue(conn, "hot");

	}
}

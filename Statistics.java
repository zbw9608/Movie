package BoxOffice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Statistics {
	public String base_url = "http://www.cbooo.cn/search?k=";  //网站搜索页面
	public String content = "";  	//存放网页源代码
	
	String movieName = "";  //电影名字
	String movieBoxOffice = "";  //电影票房
	String movieType = "";  //电影类型
	String movieTime = "";  //电影片长
	String movieYear = "";  //电影上映时间
	String movieSystem = "";  //电影制式
	String movieCountry = "";  //电影发行国家及地区
	double DirectorBoxOffice = 0.0;  //导演的票房影响力
	double ActorBoxOffice = 0.0;  //演员的票房影响力
	double MCompanyBoxOffice = 0.0;  //制作公司的票房影响力
	double ICompanyBoxOffice = 0.0;  //发行公司的票房影响力
	
	 public Statistics(String movieName){  
		 this.movieName = movieName;
		 this.base_url += this.movieName;
		 this.content = this.getContent();
		 this.movieBoxOffice = getBoxoffice();
		 this.movieType = getType();
		 this.movieTime = getTime();
		 this.movieYear = getYear();
		 this.movieSystem = getSystem();
		 this.movieCountry = getCountry();
		 this.DirectorBoxOffice = AverageDirectorBoxoffice();
		 this.ActorBoxOffice = AverageActorBoxoffice();
		 this.MCompanyBoxOffice = AverageMcompanyBoxoffice();
		 this.ICompanyBoxOffice = AverageIcompanyBoxoffice();
	 }
	
	public String getContent (){  //返回网页源代码
		String url = this.base_url;
        String content = "";  //定义一个字符串来储存网页内容
        BufferedReader in = null;  //定义一个缓冲字符输入流
        try{
            URL realUrl = new URL(url);  //将String类型转成URL对象
            URLConnection connection = realUrl.openConnection();  //初始化一个链接到URL的连接
            connection.connect();  //开始实际的连接
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));  //初始化BufferedReader输入流来读取URL的响应
            String line ;
            while ((line = in.readLine()) != null){
                content += line + "\n";  //遍历抓取到的每一行并将其存储到content中去
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{  //使用fianlly来关闭输入流
            try{
                if (in != null){
                    in.close();
                }
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }
        String Movie_url = "http://www.cbooo.cn/m/";
        String regexMovie = "<a target=\"_blank\" href=\"http://www.cbooo.cn/m/(.*?)\" title=\""+this.movieName +"\">";
        Pattern pattern = Pattern.compile(regexMovie); 
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()){
        	String movieID = matcher.group(1);
        	Movie_url += movieID;
        	content = call(Movie_url);
        }
        return content;
    }
	
	public String call(String url){  //跳转新的网页返回网页源代码
		 String content = "";  //定义一个字符串来储存网页内容
	        BufferedReader in = null;  //定义一个缓冲字符输入流
	        try{
	            URL realUrl = new URL(url);  //将String类型转成URL对象
	            URLConnection connection = realUrl.openConnection();  //初始化一个链接到URL的连接
	            connection.connect();  //开始实际的连接
	            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));  //初始化BufferedReader输入流来读取URL的响应
	            String line ;
	            while ((line = in.readLine()) != null){
	                content += line + "\n";  //遍历抓取到的每一行并将其存储到content中去
	            }
	        }catch (Exception e){
	            e.printStackTrace();
	        }
	        finally{  //使用fianlly来关闭输入流
	            try{
	                if (in != null){
	                    in.close();
	                }
	            }catch(Exception e2){
	                e2.printStackTrace();
	            }
	        }
	        return content;
	    }
	
	public List<String> getUrl(String content){
		List<String> getUrl = new LinkedList<String>();
		String regex = null;
		regex = "<a target=\"_blank\" href=\"(.*?)\"title=";  //a标签的target="_blank"意思是在新窗口中打开被链接的文档
        Pattern pattern = Pattern.compile(regex);  //定义一个样式模板，此中使用正则表达式，括号中是要抓取的内容
        Matcher matcher = pattern.matcher(content);  //定义matcher匹配
        while (matcher.find()){
        	getUrl.add(matcher.group(1).replaceAll(" ", "").replaceAll("\n", ""));  //匹配第一个,同时去掉空格和换行符
        }
		return getUrl;
    }

	public String getBoxoffice(){
		String content = this.content;
		String boxoffice = new String();
		String regex = null;
		regex = "<span class=\"m-span\">累计票房<br />(.*?)万</span>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			boxoffice = matcher.group(1);
		}
		return boxoffice;
	}
	
	public String getType(){
		String content = this.content;
		String type = new String();
		String regex = null;
		regex = "<p>类型：(.*?)</p>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			type = matcher.group(1);
		}
		return type;
	}
	
	public String getTime(){
		String content = this.content.replace("\n", "").replace(" ", "");
		System.out.println(content);
		String time = new String();
		String regex = null;
		regex = "<p>片长：(.*?)min</p>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			time = matcher.group(1);
		}
		return time;
	}
	
	public String getYear(){
		String content = this.content.replace("\n", "").replace(" ", "");
		String year = new String();
		String regex = null;
		regex = "<p>上映时间：(.*?)</p>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			year = matcher.group(1);
		}
		return year;
	}
	
	public String getSystem(){
		String content = this.content;
		String system = new String();
		String regex = null;
		regex = "<p>制式：(.*?)</p>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			system = matcher.group(1);
		}
		return system;
	}
	
	public String getCountry(){
		String content = this.content;
		String country = new String();
		String regex = null;
		regex = "<p>国家及地区：(.*?)</p>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			country = matcher.group(1);
		}
		return country;
	}
//单个导演的票房影响力
	public double SingleDirectorBoxoffice(String Id){
		List<String> boxoffice = new LinkedList<String>();
		String regex = null;
	    String  url1 =  "http://www.cbooo.cn/Mdata/getMdata_permovie?id="+Id+"&year=0&status=2&ptype=0&pIndex=1&mtp=0";
	    String content1 = call(url1); 
		regex =  "\"BoxOffice\":\"(.*?)\"";;
	    Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content1);
		 while(matcher.find()){
			 if (matcher.group(1) != "" )
				 boxoffice.add(matcher.group(1));
			 }      
		double sum = 0;
		double Daverage = 0;
		for (int i = 0;i < boxoffice.size();i++){
			String figureStr = boxoffice.get(i).replaceAll("[^0-9]", "");
			if (!figureStr.equals("")){
		      double a = Double.parseDouble(figureStr); //将String类型强制转换成double型
		       sum += a;
			   Daverage = sum/boxoffice.size();
	   	}
			
			else continue;
			}
		return Daverage;
	}
//每个导演的票房影响力	
	public double AverageDirectorBoxoffice(){
		String content = this.content.replace("\n", "").replace(" ", "");
		List<String> Id = new LinkedList<String>();
		String regex = null;
		String regex1 = null;
		double sum = 0;
		double Daverage1 = 0;
		double singleDirector;
		regex = "<dt>导演：</dt>(.*?)<dt>主演：</dt>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		String block = "";
		if(matcher.find()){
			block = matcher.group(1);
		}
			regex1 = "<atarget=\"_blank\"href=\"http://www.cbooo.cn/p/(.*?)\"title=";
			Pattern pattern1 = Pattern.compile(regex1);
			Matcher matcher1 = pattern1.matcher(block);
			while(matcher1.find()){
				Id.add(matcher1.group(1));  //存储id
				}
			for (int i = 0;i<Id.size();i++){
			 	singleDirector = SingleDirectorBoxoffice(Id.get(i));
				sum +=  singleDirector;
				Daverage1 = sum/Id.size();
			}
		return Daverage1;
	}
	//单个演员的票房影响力
	public double SingleActorBoxoffice(String Id){
		List<String> boxoffice = new LinkedList<String>();
		String regex = null;
	    String  url1 =  "http://www.cbooo.cn/Mdata/getMdata_permovie?id="+Id+"&year=0&status=2&ptype=0&pIndex=1&mtp=0";
		String content1 = call(url1);
		regex =  "\"BoxOffice\":\"(.*?)\"";
	    Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content1);
		while(matcher.find()){
			if (matcher.group(1) != "" )
				boxoffice.add(matcher.group(1));
			}   
		double sum = 0;
		double Aaverage = 0;
		for (int i = 0;i < boxoffice.size();i++){
			String figureStr = boxoffice.get(i).replaceAll("[^0-9]", "");
			if (!figureStr.equals("")){
		      double a = Double.parseDouble(figureStr); //将String类型强制转换成double型
		       sum += a;
			   Aaverage = sum/boxoffice.size();
	   	}
			
			else continue;
			}
		return Aaverage;
	}
	//前5个演员的票房影响力
	public double AverageActorBoxoffice(){
		String content = this.content.replace("\n", "").replace(" ", "");
		List<String> Id = new LinkedList<String>();
		String regex = null;
		String regex1 = null;
		double sum = 0;
		double Aaverage1 = 0;
		double aa;
		regex = "<dt>主演：</dt>(.*?)<dt>制作公司：</dt>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		String block = "";
		if(matcher.find()){
			block = matcher.group(1);
		}
		regex1 = "<atarget=\"_blank\"href=\"http://www.cbooo.cn/p/(.*?)\"title=";
		Pattern pattern1 = Pattern.compile(regex1);
		Matcher matcher1 = pattern1.matcher(block);
		while(matcher1.find()){
			Id.add(matcher1.group(1));
		}
		if (Id.size()>5){
			for (int i = 0;i<5;i++){
				aa = SingleActorBoxoffice(Id.get(i));
				sum += aa;
				Aaverage1 = sum/5;
			}
		}
		else{
			for (int i = 0;i<Id.size();i++){
				aa = SingleActorBoxoffice(Id.get(i));
				sum += aa;
				Aaverage1 = sum/Id.size();
			}
		}
		return Aaverage1;
	}
	
	//单个制作公司的票房影响力
		public double SingleMcompanyBoxoffice(String Id){
			List<String> boxoffice = new LinkedList<String>();
			String regex = null;
			String  url1 =  "http://www.cbooo.cn/Mdata/getCompanyMovie?id="+Id+"114&type=0&year=0&pIndex=1";
			String content1 = call(url1); 
		    regex =  "\"BoxOffice\":\"(.*?)\"";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(content1);
			while(matcher.find()){
				if (matcher.group(1) != "" )
					boxoffice.add(matcher.group(1));
				}   
			double sum = 0;
			double Maverage = 0;
			for (int i = 0;i < boxoffice.size();i++){
				String figureStr = boxoffice.get(i).replaceAll("[^0-9]", "");
				if (!figureStr.equals("")){
			      double a = Double.parseDouble(figureStr); //将String类型强制转换成double型
			       sum += a;
				   Maverage = sum/boxoffice.size();
		   	}
				
				else continue;
				}
			return Maverage;
		}
	//每个制作公司的票房影响力	
		public double AverageMcompanyBoxoffice(){
			String content = this.content.replace("\n", "").replace(" ", "");
			List<String> Id = new LinkedList<String>();
			String regex = null;
			String regex1 = null;
			double sum = 0;
			double Maverage1 = 0;
			double aa;
			regex = "<dt>制作公司：</dt>(.*?)<dt>发行公司：</dt>";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(content);
			String block = "";
			if(matcher.find()){
				block = matcher.group(1);
				}
			regex1 = "<atarget=\"_blank\"href=\"http://www.cbooo.cn/c/(.*?)\"title=";
			Pattern pattern1 = Pattern.compile(regex1);
			Matcher matcher1 = pattern1.matcher(block);
			while(matcher1.find()){
				Id.add(matcher1.group(1));
			}
			for (int i = 0;i<Id.size();i++){
				aa = SingleActorBoxoffice(Id.get(i));
				sum += aa;
				Maverage1 = sum/Id.size();
			}
			return Maverage1;
		}
	
		//单个发行公司的票房影响力
				public double SingleIcompanyBoxoffice(String Id){
					List<String> boxoffice = new LinkedList<String>();
					String regex = null;
					String  url1 =  "http://www.cbooo.cn/Mdata/getCompanyMovie?id="+Id+"114&type=0&year=0&pIndex=1";
					String content1 = call(url1); 
				    regex =  "\"BoxOffice\":\"(.*?)\""																									;
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher1 = pattern.matcher(content1);
					while(matcher1.find()){
						 if (matcher1.group(1) != "" )
							boxoffice.add(matcher1.group(1));
					}   
					double sum = 0;
					double Iaverage = 0;
					for (int i = 0;i < boxoffice.size();i++){
						String figureStr = boxoffice.get(i).replaceAll("[^0-9]", "");
						if (!figureStr.equals("")){
					      double a = Double.parseDouble(figureStr); //将String类型强制转换成double型
					       sum += a;
						   	Iaverage = sum/boxoffice.size();
				   	}
						
						else continue;
						}
					return Iaverage;
				}
			//每个制作公司的票房影响力	
				public double AverageIcompanyBoxoffice(){
					String content = this.content.replace("\n", "").replace(" ", "");
					List<String> Id = new LinkedList<String>();
					String regex = null;
					String regex1 = null;
					double sum = 0;
					double Iaverage1 = 0;
					double aa;
					regex = "<dt>发行公司：</dt>(.*?)</dl>";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(content);
					String block = "";
					if(matcher.find()){
						block = matcher.group(1);
						}
					regex1 = "<atarget=\"_blank\"href=\"http://www.cbooo.cn/c/(.*?)\"title=";
					Pattern pattern1 = Pattern.compile(regex1);
					Matcher matcher1 = pattern1.matcher(block);
					while(matcher1.find()){
						Id.add(matcher1.group(1));
					}
					for (int i = 0;i<Id.size();i++){
						aa = SingleActorBoxoffice(Id.get(i));
						sum += aa;
						Iaverage1 = sum/Id.size();
					}
					return Iaverage1;
				}
	
		public void clawer(){
			
		}
	
	public void save(){
		String txtFilename = this.movieName;
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(txtFilename));
			pw.println(this.movieName);
			pw.println(this.movieBoxOffice);
			pw.println(this.movieType);
			pw.println(this.movieTime);
			pw.println(this.movieYear);
			pw.println(this.movieSystem);
			pw.println(this.movieCountry);
			pw.println(this.DirectorBoxOffice);
			pw.println(this.ActorBoxOffice);
			pw.println(this.MCompanyBoxOffice);
			pw.println(this.ICompanyBoxOffice);
			pw.close();
			System.out.println("save is ok!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	public static List<String> getMovieList(){
		String movieListFile = "movieList.txt";
		List<String> movieList = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(movieListFile));
			String line = "";
			while((line = br.readLine())!=null){
				movieList.add(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieList;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> movieList = getMovieList();
		for(String movieName: movieList){
			Statistics st = new Statistics(movieName);
			st.save();
			System.out.println(st.movieName);
			System.out.println(st.movieBoxOffice);
			System.out.println(st.movieType);
			System.out.println(st.movieTime);
			System.out.println(st.movieYear);
			System.out.println(st.movieSystem);
			System.out.println(st.movieCountry);
			System.out.println(st.DirectorBoxOffice);
			System.out.println(st.ActorBoxOffice);
			System.out.println(st.MCompanyBoxOffice);
			System.out.println(st.ICompanyBoxOffice);
		}
		
	}

}

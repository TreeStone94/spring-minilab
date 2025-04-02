package hello.core.lifecycle;


public class NetworkClient {

	private String url;

	public NetworkClient() {
		System.out.println("생성자 호출 url = " + url);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	//서비스 시작 시 호출
	public void connect() {
		System.out.println("connect: " + url);
	}

	public void call(String meesage) {
		System.out.println("call: " + url + " message: " + meesage);
	}

	//서비스 종료 시 호출
	public void disconnect() {
		System.out.println("close: " + url);
	}

	public void init(){
		System.out.println("init");
		connect();
		call("초기화 연결 메세지");
	}

	public void close()  {
		System.out.println("close");
		disconnect();
	}
}

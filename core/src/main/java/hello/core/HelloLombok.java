package hello.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@Setter
@ToString
public class HelloLombok {

	private String name;
	private int age;

	public static void main(String[] args) {
		HelloLombok helloLombok = new HelloLombok();
		helloLombok.setAge(22);

		System.out.println("args = " + helloLombok.getAge());
		System.out.println("toString " + helloLombok.toString());
	}
}

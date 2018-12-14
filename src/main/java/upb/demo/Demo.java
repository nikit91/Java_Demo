package upb.demo;

public class Demo {

	public static void main(String[] args) {
		A a = new B();
		a.noop();
	}

}

class A {
	public void noop() {
	}
}

class B extends A {

}
package com.example.jpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.as;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class CourseTest {
	private static EntityManagerFactory emf;
	private EntityManager em;
    private EntityTransaction etx;

	@BeforeAll // 딱 한번 실행됨
    static void initFactory() {
        emf = Persistence.createEntityManagerFactory("course");
    }

	@BeforeEach // 테스트 메서드 직전 마다 실행
    void initEntityManager() {
        em = emf.createEntityManager();
		etx = em.getTransaction();
    }

	@AfterEach // 테스트 메서드 직후 마다 실행
	void closeEntityManager() {
		if (em.isOpen()) {
			em.close();
		}
	}

	@AfterAll // 테스트 클래스가 끝난뒤 실행
	static void closeFactory() {
		if (emf.isOpen()) {
			emf.close();
		}
	}
	@Test
	@DisplayName("Course 생성")
	void createCourse() {
		etx.begin();

		try {
			Course course = Course.builder()
					.title("JTA")
					.instructor("Robbie")
					.cost(22222.1)
					.build();
			em.persist(course);

			etx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			etx.rollback();
		}
	}

	@Test
	@DisplayName("Course 조회")
	void readCourse() {
		etx.begin();

		try {
			Course course = em.find(Course.class, 1L);
			System.out.println("course.getId() = " + course.getId());
			System.out.println("course.getTitle() = " + course.getTitle());
			System.out.println("course.getInstructor() = " + course.getInstructor());

			// 같은 내용을 조회할 경우 쿼리가 생성되지 않음.
			// 우선 1차 캐시 조회 후, 데이터가 없을 경우에만 DB에 쿼리를 날리기 때문.
			Course course1 = em.find(Course.class, 1L);
			System.out.println("course1.getId() = " + course1.getId());
			System.out.println("course1.getTitle() = " + course1.getTitle());
			System.out.println("course1.getInstructor() = " + course1.getInstructor());

			System.out.println("(course == course1) = " + (course == course1));

			Course course3 = em.find(Course.class, 2L);
			System.out.println("course3.getId() = " + course3.getId());
			System.out.println("course3.getTitle() = " + course3.getTitle());
			System.out.println("course3.getInstructor() = " + course3.getInstructor());

			etx.commit();
		} catch (Exception e) {
			etx.rollback();
		}
	}

	@Test
	@DisplayName("Course 수정")
	void updateCourse() {
		etx.begin();
		try {
			Course course = em.find(Course.class, 1L);
			course.setTitle("Spring");

			// course 수정 후, em.persist(course)를 해주지 않아도 수정이 됨 -> 변경감지에 의해서
			// @Service 에서 update 이후 courseRepository.save(); 와 같은 과정을 수행하지 않는 이유이기도 하다.
			assertThat(em.contains(course)).isTrue(); // 영속화 확인

			// 준영속
			em.detach(course);
			assertThat(em.contains(course)).isFalse();

			// merge() : 조회해서 영속화를 하고, 그 다음에 병합을 하고, 반환한다.
			// 변경할 데이터 객체
			Course mergedCourse = Course.builder()
					.title("Spring AOP")
					.instructor("Sparta")
					.cost(11.0)
					.build();
			mergedCourse.setId(course.getId());
			em.merge(mergedCourse); // 영속화 -> 병합(update) -> 반환
			assertThat(em.contains(mergedCourse)).isFalse();

			etx.commit();
		} catch (Exception e) {
			etx.rollback();
		}
	}

	@Test
	@DisplayName("Course 삭제")
	void deleteCourse() {
		etx.begin();

		try {
			Course course = em.find(Course.class, 1L);
			assertThat(em.contains(course)).isTrue();

			em.remove(course);
			assertThat(em.contains(course)).isFalse();

			etx.commit();
		} catch (Exception e) {
			etx.rollback();
		}
	}



}
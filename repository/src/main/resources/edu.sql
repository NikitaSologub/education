-- не думаю что удалить схему public и создать заново хорошая идея.
-- оставим закоментированные

-- DROP SCHEMA public;
-- CREATE SCHEMA public AUTHORIZATION postgres;

-- создаем sequences если их до этого не было (если были то удаляем предварительно)
DROP SEQUENCE IF EXISTS public.credential_id_seq;
CREATE SEQUENCE public.credential_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

DROP SEQUENCE IF EXISTS public.group_id_seq;
CREATE SEQUENCE public.group_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

DROP SEQUENCE IF EXISTS public.mark_id_seq;
CREATE SEQUENCE public.mark_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

DROP SEQUENCE IF EXISTS public.salary_id_seq;
CREATE SEQUENCE public.salary_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

DROP SEQUENCE IF EXISTS public.subject_id_seq;
CREATE SEQUENCE public.subject_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

DROP SEQUENCE IF EXISTS public.user_id_seq;
CREATE SEQUENCE public.user_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    START 1
    CACHE 1
    NO CYCLE;

-- создаем тадлицы если их до этого не было (если были то удаляем предварительно)
DROP TABLE IF EXISTS public.credential;
CREATE TABLE public.credential
(
    id         int4    NOT NULL GENERATED ALWAYS AS IDENTITY,
    login      varchar NOT NULL,
    "password" varchar NOT NULL,
    CONSTRAINT credential_pk PRIMARY KEY (id),
    CONSTRAINT credential_un UNIQUE (login)
);

DROP TABLE IF EXISTS public."group";
CREATE TABLE public."group"
(
    id          int4    NOT NULL GENERATED ALWAYS AS IDENTITY,
    title       varchar NOT NULL,
    teacher_id  int4    NULL,
    description varchar NOT NULL,
    CONSTRAINT group_pk PRIMARY KEY (id),
    CONSTRAINT group_un UNIQUE (title)
);

DROP TABLE IF EXISTS public.group_student;
CREATE TABLE public.group_student
(
    group_id   int4 NOT NULL,
    student_id int4 NOT NULL
);

DROP TABLE IF EXISTS public.group_subject;
CREATE TABLE public.group_subject
(
    group_id   int4 NOT NULL,
    subject_id int4 NOT NULL
);

DROP TABLE IF EXISTS public.mark;
CREATE TABLE public.mark
(
    id         int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
    student_id int4 NOT NULL,
    subject_id int4 NOT NULL,
    "date"     date NOT NULL,
    point      int4 NOT NULL,
    CONSTRAINT mark_pk PRIMARY KEY (id)
);

DROP TABLE IF EXISTS public.salary;
CREATE TABLE public.salary
(
    id           int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
    coins_amount int4 NOT NULL DEFAULT 0,
    "date"       date NOT NULL,
    teacher_id   int4 NOT NULL,
    CONSTRAINT salary_pk PRIMARY KEY (id)
);

DROP TABLE IF EXISTS public.subject;
CREATE TABLE public.subject
(
    id    int4    NOT NULL GENERATED ALWAYS AS IDENTITY,
    title varchar NOT NULL,
    CONSTRAINT subject_pk PRIMARY KEY (id),
    CONSTRAINT subject_un UNIQUE (title)
);

DROP TABLE IF EXISTS public.person;
CREATE TABLE public.person
(
    id            int4    NOT NULL DEFAULT nextval('user_id_seq'::regclass),
    firstname     varchar NOT NULL,
    lastname      varchar NOT NULL,
    patronymic    varchar NOT NULL,
    date_of_birth date    NOT NULL,
    credential_id int4    NOT NULL,
    role       varchar    NOT NULL,
    CONSTRAINT person_un_cred UNIQUE (credential_id),
    CONSTRAINT user_pk PRIMARY KEY (id),
    CONSTRAINT user_cred_fk FOREIGN KEY (credential_id) REFERENCES public.credential (id)
);

-- наполняем таблицы данными (часть тех таблиц, с которыми уже работает моя программа)
INSERT INTO public.credential (login, "password")
VALUES ('RUTS', '123'),
       ('ADMIN', '234'),
       ('eee', '123'),
       ('Stolar55', '123'),
       ('BabkaVKedah', '123'),
       ('GG', '456'),
       ('AXEL23', '123'),
       ('eki', '123');

INSERT INTO public.person (firstname, lastname, patronymic, date_of_birth, credential_id, role)
VALUES ('Анастасия', 'Столярчук', 'Ивановна', '1995-08-17', 4, 'STUDENT'),
       ('Ярец', 'Илья', 'Викторович', '1995-06-22', 3, 'STUDENT'),
       ('Галина', 'Ахраменко', 'Генадьевна', '1959-03-28', 1, 'TEACHER'),
       ('Егор', 'Татур', 'Степанович', '1995-06-15', 38, 'STUDENT'),
       ('Юрий', 'Екимов', 'Васильевич', '1971-07-16', 34, 'TEACHER'),
       ('Любовь', 'Руцкая', 'Валерьевна', '1982-07-16', 35, 'TEACHER'),
       ('Никита', 'Сологуб', 'Олегович', '1992-04-23', 57, 'ADMIN'),
       ('Сергей', 'Брин', 'Петрович', '1997-12-11', 53, 'STUDENT');

INSERT INTO public.salary (coins_amount, "date", teacher_id)
VALUES (64140, '2021-01-12', 1),
       (63715, '2021-02-11', 1),
       (61898, '2021-03-13', 1),
       (64140, '2021-04-12', 2),
       (63716, '2021-05-12', 2),
       (61898, '2021-06-17', 2),
       (66780, '2021-04-15', 1),
       (78560, '2020-06-19', 25),
       (76130, '2021-02-13', 25),
       (74060, '2020-12-27', 25),
       (50403, '2021-01-01', 1),
       (43085, '2020-10-23', 24);

INSERT INTO public.subject (title)
VALUES ('Математика'),
       ('Физика'),
       ('Биология'),
       ('Тригонометрия'),
       ('Алгебра');
-- =========================================================================
-- CATALOG SERVICE DATABASE
-- =========================================================================
CREATE DATABASE IF NOT EXISTS coursecart_catalog_db;
USE coursecart_catalog_db;

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    UNIQUE KEY uk_categories_name (name)
) ENGINE=InnoDB;

CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    original_price DECIMAL(10,2) DEFAULT NULL,
    instructor_name VARCHAR(255),
    rating DECIMAL(3,1) DEFAULT 0.0,
    rating_count INT DEFAULT 0,
    is_bestseller BOOLEAN DEFAULT FALSE,

    status ENUM('DRAFT', 'ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'DRAFT',
    FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

CREATE TABLE lessons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    display_order INT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id)
) ENGINE=InnoDB;

CREATE INDEX idx_courses_status ON courses(status);
CREATE INDEX idx_courses_category_id ON courses(category_id);

-- CATALOG SERVICE SEED DATA
USE coursecart_catalog_db;

INSERT INTO categories (id, name) VALUES 
(1001, 'Software Engineering'),
(1002, 'Cloud Computing'),
(1003, 'Data Science'),
(1004, 'Artificial Intelligence'),
(1005, 'Web Development');

INSERT INTO courses (id, category_id, title, description, price, original_price, instructor_name, rating, rating_count, is_bestseller, status) VALUES 
(1001, 1005, 'Java Fundamentals', 'Learn Java from scratch', 49.99, 79.99, 'Prof. John Doe', 4.8, 1500, true, 'ACTIVE'),
(1003, 1005, 'Angular for Enterprise', 'Master component-driven architecture and state management.', 59.99, 99.99, 'Max S.', 4.9, 3200, true, 'ACTIVE'),
(1005, 1005, 'Spring Boot Microservices', 'Build scalable backends with Java and Spring Cloud.', 69.99, 129.99, 'Koushikk', 4.8, 5400, true, 'ACTIVE'),
(1014, 1001, 'Agile Software Development', 'Skills youll gain: User Story, Agile Software Development...', 39.99, 69.99, 'University of Minnesota', 4.7, 7500, false, 'ACTIVE'),
(1015, 1001, 'Software Design and Architecture', 'Skills youll gain: Software Architecture, Model View Controller...', 45.99, 79.99, 'University of Alberta', 4.6, 4000, false, 'ACTIVE'),
(1017, 1001, 'Microsoft Full-Stack Developer', 'Skills youll gain: Microsoft Copilot, CI/CD, Cascading Style Sheets...', 59.99, 119.99, 'Microsoft', 4.6, 531, false, 'ACTIVE'),
(1018, 1002, 'Introduction to Information Technology and AWS Cloud', 'Skills youll gain: Amazon Web Services, Web Applications...', 39.99, 69.99, 'Amazon Web Services', 4.8, 541, false, 'ACTIVE'),
(1020, 1002, 'AWS Cloud Technical Essentials', 'Skills youll gain: AWS Identity and Access Management...', 49.99, 79.99, 'Amazon Web Services', 4.8, 6300, false, 'ACTIVE'),
(1021, 1002, 'AWS Cloud Solutions Architect', 'Skills youll gain: AWS Identity and Access Management, Data Lakes...', 89.99, 149.99, 'Amazon Web Services', 4.8, 7100, true, 'ACTIVE'),
(1010, 1003, 'Google Data Analytics', 'Skills youll gain: Data Storytelling, Rmarkdown, Data Visualization...', 49.99, 89.99, 'Google', 4.8, 182000, true, 'ACTIVE'),
(1011, 1003, 'Microsoft Data Analysis with SQL, Excel & Power BI', 'Skills youll gain: Data Visualization, Data Storytelling, Power BI...', 59.99, 99.99, 'Microsoft', 4.6, 1500, false, 'ACTIVE'),
(1012, 1003, 'Python for Data Science, AI & Development', 'Beginner Course. 5 weeks of study, 3-6 hours per week.', 39.99, 79.99, 'IBM', 4.6, 44000, false, 'ACTIVE'),
(1022, 1004, 'Claude Code: Software Engineering with Generative AI Agents', 'Skills youll gain: Claude Code, Anthropic Claude, Multimodal Prompts...', 39.99, 69.99, 'Vanderbilt University', 4.7, 174, false, 'ACTIVE'),
(1023, 1004, 'AI Agents and Agentic AI with Python & Generative AI', 'Skills youll gain: Agentic systems, Generative AI Agents, Agentic Workflows...', 49.99, 89.99, 'Vanderbilt University', 4.6, 475, false, 'ACTIVE'),
(1024, 1004, 'Fundamentals of Machine Learning and Artificial Intelligence', 'Skills youll gain: Artificial Intelligence and Machine Learning (AI/ML)...', 0.0, 29.99, 'Amazon Web Services', 4.6, 3900, false, 'ACTIVE');

INSERT INTO lessons (id, course_id, title, content, display_order) VALUES 
(1, 1001, 'Introduction to Java Fundamentals', 'This is the content for Introduction to Java Fundamentals.', 1),
(2, 1001, 'Core Concepts of Java Fundamentals', 'This is the content for Core Concepts of Java Fundamentals.', 2),
(3, 1001, 'Advanced Features in Java Fundamentals', 'This is the content for Advanced Features in Java Fundamentals.', 3),
(4, 1001, 'Building a Project with Java Fundamentals', 'This is the content for Building a Project with Java Fundamentals.', 4),
(5, 1003, 'Introduction to Angular for Enterprise', 'This is the content for Introduction to Angular for Enterprise.', 1),
(6, 1003, 'Core Concepts of Angular for Enterprise', 'This is the content for Core Concepts of Angular for Enterprise.', 2),
(7, 1003, 'Advanced Features in Angular for Enterprise', 'This is the content for Advanced Features in Angular for Enterprise.', 3),
(8, 1003, 'Building a Project with Angular for Enterprise', 'This is the content for Building a Project with Angular for Enterprise.', 4),
(9, 1005, 'Introduction to Spring Boot Microservices', 'This is the content for Introduction to Spring Boot Microservices.', 1),
(10, 1005, 'Core Concepts of Spring Boot Microservices', 'This is the content for Core Concepts of Spring Boot Microservices.', 2),
(11, 1005, 'Advanced Features in Spring Boot Microservices', 'This is the content for Advanced Features in Spring Boot Microservices.', 3),
(12, 1005, 'Building a Project with Spring Boot Microservices', 'This is the content for Building a Project with Spring Boot Microservices.', 4),
(13, 1014, 'Introduction to Agile Software Development', 'This is the content for Introduction to Agile Software Development.', 1),
(14, 1014, 'Core Concepts of Agile Software Development', 'This is the content for Core Concepts of Agile Software Development.', 2),
(15, 1014, 'Advanced Features in Agile Software Development', 'This is the content for Advanced Features in Agile Software Development.', 3),
(16, 1014, 'Building a Project with Agile Software Development', 'This is the content for Building a Project with Agile Software Development.', 4),
(17, 1015, 'Introduction to Software Design and Architecture', 'This is the content for Introduction to Software Design and Architecture.', 1),
(18, 1015, 'Core Concepts of Software Design and Architecture', 'This is the content for Core Concepts of Software Design and Architecture.', 2),
(19, 1015, 'Advanced Features in Software Design and Architecture', 'This is the content for Advanced Features in Software Design and Architecture.', 3),
(20, 1015, 'Building a Project with Software Design and Architecture', 'This is the content for Building a Project with Software Design and Architecture.', 4),
(21, 1017, 'Introduction to Microsoft Full-Stack Developer', 'This is the content for Introduction to Microsoft Full-Stack Developer.', 1),
(22, 1017, 'Core Concepts of Microsoft Full-Stack Developer', 'This is the content for Core Concepts of Microsoft Full-Stack Developer.', 2),
(23, 1017, 'Advanced Features in Microsoft Full-Stack Developer', 'This is the content for Advanced Features in Microsoft Full-Stack Developer.', 3),
(24, 1017, 'Building a Project with Microsoft Full-Stack Developer', 'This is the content for Building a Project with Microsoft Full-Stack Developer.', 4),
(25, 1018, 'Introduction to AWS Cloud', 'This is the content for Introduction to AWS Cloud.', 1),
(26, 1018, 'Core Concepts of AWS Cloud', 'This is the content for Core Concepts of AWS Cloud.', 2),
(27, 1018, 'Advanced Features in AWS Cloud', 'This is the content for Advanced Features in AWS Cloud.', 3),
(28, 1018, 'Building a Project with AWS Cloud', 'This is the content for Building a Project with AWS Cloud.', 4),
(29, 1020, 'Introduction to AWS Cloud Technical Essentials', 'This is the content for Introduction to AWS Cloud Technical Essentials.', 1),
(30, 1020, 'Core Concepts of AWS Cloud Technical Essentials', 'This is the content for Core Concepts of AWS Cloud Technical Essentials.', 2),
(31, 1020, 'Advanced Features in AWS Cloud Technical Essentials', 'This is the content for Advanced Features in AWS Cloud Technical Essentials.', 3),
(32, 1020, 'Building a Project with AWS Cloud Technical Essentials', 'This is the content for Building a Project with AWS Cloud Technical Essentials.', 4),
(33, 1021, 'Introduction to AWS Cloud Solutions Architect', 'This is the content for Introduction to AWS Cloud Solutions Architect.', 1),
(34, 1021, 'Core Concepts of AWS Cloud Solutions Architect', 'This is the content for Core Concepts of AWS Cloud Solutions Architect.', 2),
(35, 1021, 'Advanced Features in AWS Cloud Solutions Architect', 'This is the content for Advanced Features in AWS Cloud Solutions Architect.', 3),
(36, 1021, 'Building a Project with AWS Cloud Solutions Architect', 'This is the content for Building a Project with AWS Cloud Solutions Architect.', 4),
(37, 1010, 'Introduction to Google Data Analytics', 'This is the content for Introduction to Google Data Analytics.', 1),
(38, 1010, 'Core Concepts of Google Data Analytics', 'This is the content for Core Concepts of Google Data Analytics.', 2),
(39, 1010, 'Advanced Features in Google Data Analytics', 'This is the content for Advanced Features in Google Data Analytics.', 3),
(40, 1010, 'Building a Project with Google Data Analytics', 'This is the content for Building a Project with Google Data Analytics.', 4),
(41, 1011, 'Introduction to Microsoft Data Analysis', 'This is the content for Introduction to Microsoft Data Analysis.', 1),
(42, 1011, 'Core Concepts of Microsoft Data Analysis', 'This is the content for Core Concepts of Microsoft Data Analysis.', 2),
(43, 1011, 'Advanced Features in Microsoft Data Analysis', 'This is the content for Advanced Features in Microsoft Data Analysis.', 3),
(44, 1011, 'Building a Project with Microsoft Data Analysis', 'This is the content for Building a Project with Microsoft Data Analysis.', 4),
(45, 1012, 'Introduction to Python for Data Science', 'This is the content for Introduction to Python for Data Science.', 1),
(46, 1012, 'Core Concepts of Python for Data Science', 'This is the content for Core Concepts of Python for Data Science.', 2),
(47, 1012, 'Advanced Features in Python for Data Science', 'This is the content for Advanced Features in Python for Data Science.', 3),
(48, 1012, 'Building a Project with Python for Data Science', 'This is the content for Building a Project with Python for Data Science.', 4),
(49, 1022, 'Introduction to Claude Code', 'This is the content for Introduction to Claude Code.', 1),
(50, 1022, 'Core Concepts of Claude Code', 'This is the content for Core Concepts of Claude Code.', 2),
(51, 1022, 'Advanced Features in Claude Code', 'This is the content for Advanced Features in Claude Code.', 3),
(52, 1022, 'Building a Project with Claude Code', 'This is the content for Building a Project with Claude Code.', 4),
(53, 1023, 'Introduction to AI Agents', 'This is the content for Introduction to AI Agents.', 1),
(54, 1023, 'Core Concepts of AI Agents', 'This is the content for Core Concepts of AI Agents.', 2),
(55, 1023, 'Advanced Features in AI Agents', 'This is the content for Advanced Features in AI Agents.', 3),
(56, 1023, 'Building a Project with AI Agents', 'This is the content for Building a Project with AI Agents.', 4),
(57, 1024, 'Introduction to Machine Learning', 'This is the content for Introduction to Machine Learning.', 1),
(58, 1024, 'Core Concepts of Machine Learning', 'This is the content for Core Concepts of Machine Learning.', 2),
(59, 1024, 'Advanced Features in Machine Learning', 'This is the content for Advanced Features in Machine Learning.', 3),
(60, 1024, 'Building a Project with Machine Learning', 'This is the content for Building a Project with Machine Learning.', 4);


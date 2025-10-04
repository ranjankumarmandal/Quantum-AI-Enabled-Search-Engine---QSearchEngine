# Quantum AI Enabled Search Engine

This project is the **backend prototype** for a custom search engine.  
It provides REST APIs for crawling web pages, indexing their content using **Apache Lucene**, and retrieving ranked search results.  
The goal is to build a foundation similar to a classical search engine, ready for future integration with **LLM** and **Quantum-AI-based** modules.

---

## 🚀 Features
- **Crawling API** – fetches and indexes pages from a given seed URL
- **Lucene-based indexing and BM25 retrieval** – stores title, URL, and body snippet
- **REST Search API** – simple `/api/search?q=...` interface for frontend clients
- **Pagination support** – `page` and `size` parameters
- **Configurable index path** – via `app.index.path` in `application.properties`
- **Work in progress** – hooks for LLM-based query rewriting and Quantum AI -inspired ranking (planned for Step 2)

---

## 🛠 Tech Stack
| Component       | Technology |
|-----------------|------------|
| Language        | Java 17 |
| Framework       | Spring Boot 3.x |
| Build Tool      | Maven |
| Search Library  | Apache Lucene 9.11.1 |
| HTML Parser     | Jsoup |
| REST & JSON     | Spring Web / Jackson |
| Container-ready | Spring Boot Maven Plugin |
| Future Add-ons  | LLM-based query expansion, Quantum-inspired re-ranking |

## You have some query?
If you have some query, feel free to connect with me here -- [Ranjan Kumar Mandal](https://www.linkedin.com/in/ranjan-kumar-m-818367158/)

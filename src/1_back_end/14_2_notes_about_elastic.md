# Источники
* Хорошее введение https://xakep.ru/2015/06/11/elasticsearch-tutorial/

# Что это
# Понятия
* **Стемминг** — это нахождение основы слова для заданного исходного слова. Основа необязательно совпадает с морфологическим корнем слова.
* **Лемматизация** — приведение слова к нормальной (словарной) форме. Для существительных это именительный падеж и единственное число.
* **Корпус** — набор текстов собранных по каким-то критериям и разделенных по категориям, обеспеченных какой-либо поисковой системой.
* **Параллельный корпус** — 1+ корпусов, которым соотв. другие корпусы, напр. тексты на нескольких языках соотв. друг другу (их предложения несут одинаковый смысл и соотв. друг другу).
* **Стоп-слова** (**шумовые слова**) — предлоги, суффиксы, междометия, цифры, частицы и подобное. Общие шумовые слова всегда исключаются из поискового запроса (кроме поиска по строгому соответствию поисковой фразы), также они игнорируются при построении инвертированного индекса.
* **N-грамма** — последовательность из n элементов. С семантической точки зрения это может быть последовательность звуков, слогов, слов или букв
* **multitenancy** - возможность организовать несколько различных поисковых систем в рамках одного объекта Elasticsearch (напр. несколько наборов правил для разных языков, правила применяются в том числе при индексировании данных автоматически в зависимости от настроек и типа загружаемых данных)
* **Операционная стабильность** — на каждое изменение данных в хранилище ведется логирование сразу на нескольких ячейках кластера для повышения отказоустойчивости и сохранности данных в случае разного рода сбоев.
* **Отсутствие схемы (schema-free)** — Elasticsearch позволяет загружать в него обычный JSON-объект, а далее он уже сам все проиндексирует, добавит в базу поиска. Позволяет не заморачиваться слишком сильно над структурой данных при быстром прототипировании.
* индекс - 
* инвертированный индекса - 
* Нечеткий поиск - 
* CJK - 
* Анализаторы

# REST ful Api в Elastic Search
# Использование со Spring Data Elastic Search

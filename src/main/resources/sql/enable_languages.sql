UPDATE languages SET enable = false;

UPDATE languages SET enable = true
WHERE name IN ('1C Enterprise', 'C', 'C#', 'C++', 'CSS', 'Dart',
'Go', 'HTML', 'Java', 'JavaScript', 'Kotlin', 'PHP', 'Python',
'Ruby', 'Rust', 'Scala', 'Swift', 'TypeScript');
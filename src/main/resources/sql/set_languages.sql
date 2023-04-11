UPDATE languages SET enable = false;

UPDATE languages SET enable = true
WHERE name IN ('C', 'C#', 'C++', 'CSS', 'Dart',
'Go', 'HTML', 'Java', 'JavaScript', 'Kotlin', 'Perl', 'PHP', 'Python',
'Ruby', 'Rust', 'Scala', 'Swift', 'TypeScript');
UPDATE languages SET show_in_menu = false;

UPDATE languages SET show_in_menu = true
WHERE name IN ('C', 'C#', 'C++', 'CSS', 'Dart',
'Go', 'HTML', 'Java', 'JavaScript', 'Kotlin', 'Perl', 'PHP', 'Python',
'Ruby', 'Rust', 'Scala', 'Swift', 'TypeScript');
UPDATE languages SET show_in_menu = false;

UPDATE languages SET show_in_menu = true
WHERE name IN ('1C Enterprise', 'C', 'C#', 'C++', 'CSS', 'Dart',
'Go', 'HTML', 'Java', 'JavaScript', 'Kotlin', 'PHP', 'Python',
'Ruby', 'Rust', 'Scala', 'Swift', 'TypeScript');
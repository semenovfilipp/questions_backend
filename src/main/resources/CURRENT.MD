1. Создаем объект User (id, name, password,active,set<role>) +
2. create role -> user,admin +
3. Далее в User на role:
4. создаем elementColection(тип подгрузки eager)
5. CollectionTable(user) -> создать связь между user_table и user_id
6. Делаем Enumerated по типу String
7. В SpringSecurityConfig добавт
8. 
TestUserLogin

- testCanLoginWithValidCredentials -
тест Можно войти в систему с правильными учетными данными

- testCanLoginWithRandomValidCredentials
тест Можно ли войти в систему с помощью случайных действительных учетных данных

- testCannotLoginWithInvalidEmail
тест Невозможно войти с неверным адресом электронной почты

- testCannotLoginWithShortPassword
тест Невозможно войти с неправильным паролем

- testCannotLoginWithWrongPassword
тест Невозможно войти в систему с неправильным паролем

- testCannotLoginWithNonExistentUser
тест Невозможно войти в систему с несуществующим пользователем

- testCannotLoginWithEmptyCredentials
тест Невозможно войти в систему с пустыми учетными данными

-?? testCannotLoginWithNullCredentials
// Создаем payload без установки email и password (null значения)

- testLoginResponseStructure
структура ответа на вход в систему

- testLoginWithUnverifiedUser
тестовый вход с непроверенным пользователем
// Тест для пользователя, который не подтвердил email

- testLoginWithBannedUser
тестовый вход с запрещенным пользователем
// Тест для заблокированного пользователя
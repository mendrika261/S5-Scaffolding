# Overview 🔮

Java CLI tool to scaffold a project with full CRUD from a database schema.
It is based on a template, so you can customize and extend it with any language you want.

> 🎉 It works now with [my web framework](https://github.com/mendrika261/S4-Java-Framework) and [unidao](https://github.com/mendrika261/S5-UniDao)

https://github.com/mendrika261/S5-Scaffolding/assets/97053149/1d19fa53-c1ac-4739-ae98-088040ee4b78

## Already built 🛠️

- Postgres, Mysql database syntax
- My framework 👉 [See here](https://github.com/mendrika261/S4-Java-Framework)
- Spring Rest
- Angular standalone app
- ... template and class YOU can override

## How to use 🚀

1. Download the zip file from the release and extract
2. Change the `config.json` file to match your database configuration

```
...
  "databases": {
    "default": {
      "provider": "postgresql",
      "host": "localhost",
      "port": "5432",
      "database": "test",
      "user": "mendrika261",
      "password": ""
    }
  },
...
```

3. (Optional) Change files in the `templates` folder to match your needs
4. Run the jar file with the following command and follow the instructions

```shell
java -jar scaffolding.jar
```

## How to build 🏗️

1. Clone the repository
2. Run the following command

```shell
mvn clean package
```

3. The jar file will be in the `target` folder
4. Follow the instructions in the `How to use 2` section

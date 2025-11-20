# AT02 - Álcool ou Gasolina

https://github.com/RamonNicolasGL/AT02AlcoolOuGasolinaEstendido.git

Aplicativo Android simples que calcula qual combustível é mais vantajoso: álcool ou gasolina.

## Funcionalidades

### Cálculo de Combustível
- Comparação entre álcool e gasolina
- Alternância entre percentuais de referência (70% ou 75%)
- Entrada decimal com vírgula ou ponto
- Validação de dados

### Persistência de Estado
- Salvamento automático do estado do switch
- Restauração via SharedPreferences

### Gerenciamento de Postos
- CRUD completo de postos de combustível
- Armazenamento com SharedPreferences

### Lista de Postos 
- RecyclerView com todos os postos cadastrados
- Visualização de detalhes completos
- Edição e exclusão de postos
- Navegação para tela de detalhes

### Localização e Mapas
- Solicitação de permissões de localização em runtime
- Captura automática de coordenadas GPS
- Visualização no Google Maps via Intent

### Internacionalização
- Suporte a português (BR) e inglês (EN)
- Troca automática baseada no idioma do sistema


## Tecnologias

- Kotlin
- Android SDK
- Material Design Components
- SharedPreferences para persistência de dados
- Gson para serialização JSON
- FusedLocationProviderClient
- RecyclerView
- ViewBinding

## Estrutura do projeto

```
./app/src/main
├── AndroidManifest.xml
├── java
│   └── com
│       └── example
│           └── alcoolougasolinaestendido
│               ├── DetalhesPostoActivity.kt
│               ├── ListaPostosActivity.kt
│               ├── MainActivity.kt
│               ├── PostoAdapter.kt
│               ├── Posto.kt
│               └── PostoRepository.kt
└── res
    ├── drawable
    │   ├── ic_launcher_background.xml
    │   └── ic_launcher_foreground.xml
    ├── layout
    │   ├── activity_detalhes_posto.xml
    │   ├── activity_lista_postos.xml
    │   ├── activity_main.xml
    │   └── item_posto.xml
    ├── mipmap-anydpi-v26
    │   ├── ic_launcher_round.xml
    │   └── ic_launcher.xml
    ├── mipmap-hdpi
    │   ├── ic_launcher_round.webp
    │   └── ic_launcher.webp
    ├── mipmap-mdpi
    │   ├── ic_launcher_round.webp
    │   └── ic_launcher.webp
    ├── mipmap-xhdpi
    │   ├── ic_launcher_round.webp
    │   └── ic_launcher.webp
    ├── mipmap-xxhdpi
    │   ├── ic_launcher_round.webp
    │   └── ic_launcher.webp
    ├── mipmap-xxxhdpi
    │   ├── ic_launcher_round.webp
    │   └── ic_launcher.webp
    ├── values
    │   ├── colors.xml
    │   ├── strings.xml
    │   └── themes.xml
    ├── values-en
    │   └── strings.xml
    ├── values-night
    │   └── themes.xml
    └── xml
        ├── backup_rules.xml
        └── data_extraction_rules.xml
```

## Requisitos

- Android Studio Narwhal ou superior
- Gradle 8.x
- Android SDK mínimo: verificar em `build.gradle.kts`

## Build

```bash
./gradlew assembleDebug
```

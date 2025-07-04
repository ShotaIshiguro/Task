# Task Management Application

Spring Boot + MyBatis + NEON DB + Tailwind CSS を使用したタスク管理アプリケーション

## 技術スタック

- **Backend**: Spring Boot 3.5.0
- **Database**: NEON DB (PostgreSQL)
- **ORM**: MyBatis
- **Frontend**: Thymeleaf + Tailwind CSS
- **Build Tool**: Maven
- **Java Version**: 17

## プロジェクト構造

### 画面単位のディレクトリ構造

各画面（機能）は以下の構造でディレクトリを作成します：

```
src/main/java/com/task/kanri/tools/tasks/
├── SC_01/                    # 画面ID（例：SC_01 = 取引先管理画面）
│   ├── controller/           # コントローラークラス
│   │   └── CustomerController.java
│   ├── service/              # ビジネスロジック
│   ├── repository/           # データアクセス層（MyBatis Mapper）
│   └── form/                 # フォームクラス（入力値検証用）
│       ├── CustomerForm.java
│       └── CustomerSearchForm.java
├── SC_02/                    # 画面ID（例：SC_02 = タスク管理画面）
│   ├── controller/
│   ├── service/
│   ├── repository/
│   └── form/
└── ...
```

### ディレクトリの役割

#### controller/
- HTTPリクエストの処理
- 画面遷移の制御
- モデルへのデータ設定

#### service/
- ビジネスロジックの実装
- トランザクション管理
- 複数のリポジトリの組み合わせ

#### repository/
- データベースアクセス
- MyBatis Mapperインターフェース
- SQLクエリの定義

#### form/
- 入力フォームのバリデーション
- 画面間でのデータ受け渡し
- 入力値の型変換

### テンプレート構造

```
src/main/resources/templates/
├── layout/
│   └── base.html            # 共通レイアウト（サイドナビゲーション付き）
├── customers/
│   ├── list.html            # 取引先一覧画面
│   ├── create.html          # 取引先作成画面
│   └── edit.html            # 取引先編集画面
├── tasks/
│   └── list.html            # タスク一覧画面
├── work-hours/
│   └── list.html            # 稼働管理画面
└── home.html                # ダッシュボード
```

## 画面一覧

| 画面ID | 画面名 | URL | 説明 |
|--------|--------|-----|------|
| SC_01 | 取引先一覧 | `/customers` | 取引先の一覧表示・管理 |
| SC_02 | タスク一覧 | `/tasks` | タスクの一覧表示・管理 |
| SC_03 | 稼働管理 | `/work-hours` | 稼働時間の記録・管理 |

## 開発ガイドライン

### Formクラスの基本設計

#### 1. Formクラスの作成

```java
// src/main/java/com/task/kanri/tools/tasks/SC_01/form/CustomerForm.java
package com.task.kanri.tools.tasks.SC_01.form;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class CustomerForm {
    
    private Long id; // 編集時のみ使用
    
    @NotBlank(message = "取引先名は必須です")
    @Size(max = 100, message = "取引先名は100文字以内で入力してください")
    private String name;
    
    @NotBlank(message = "担当者名は必須です")
    @Size(max = 50, message = "担当者名は50文字以内で入力してください")
    private String contactPerson;
    
    @NotBlank(message = "電話番号は必須です")
    @Size(max = 20, message = "電話番号は20文字以内で入力してください")
    private String phone;
    
    @Email(message = "メールアドレスの形式が正しくありません")
    @Size(max = 100, message = "メールアドレスは100文字以内で入力してください")
    private String email;
    
    @Size(max = 500, message = "備考は500文字以内で入力してください")
    private String notes;
}
```

#### 2. 検索用Formクラスの作成

```java
// src/main/java/com/task/kanri/tools/tasks/SC_01/form/CustomerSearchForm.java
package com.task.kanri.tools.tasks.SC_01.form;

import lombok.Data;

@Data
public class CustomerSearchForm {
    
    private String name;        // 取引先名（部分一致）
    private String contactPerson; // 担当者名（部分一致）
    private String status;      // ステータス（完全一致）
    private String sortBy;      // ソート項目
    private String sortOrder;   // ソート順（asc/desc）
}
```

### コントローラーでのForm使用

#### 1. 一覧表示（GET）

```java
@Controller
@RequestMapping("/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping
    public String list(@ModelAttribute CustomerSearchForm searchForm, Model model) {
        // 検索条件に基づいてデータを取得
        List<Customer> customers = customerService.searchCustomers(searchForm);
        
        // モデルにデータを設定
        model.addAttribute("customers", customers);
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("pageTitle", "取引先一覧");
        
        return "customers/list";
    }
}
```

#### 2. 新規作成画面表示（GET）

```java
@GetMapping("/create")
public String createForm(Model model) {
    // 空のFormをモデルに設定
    model.addAttribute("customerForm", new CustomerForm());
    model.addAttribute("pageTitle", "取引先作成");
    
    return "customers/create";
}
```

#### 3. 新規作成処理（POST）

```java
@PostMapping("/create")
public String create(@Valid @ModelAttribute CustomerForm customerForm, 
                    BindingResult bindingResult, 
                    Model model) {
    
    if (bindingResult.hasErrors()) {
        // バリデーションエラーの場合、作成画面に戻る
        model.addAttribute("pageTitle", "取引先作成");
        return "customers/create";
    }
    
    // 正常な場合、サービスを呼び出して保存
    customerService.createCustomer(customerForm);
    
    // 一覧画面にリダイレクト
    return "redirect:/customers";
}
```

#### 4. 編集画面表示（GET）

```java
@GetMapping("/edit/{id}")
public String editForm(@PathVariable Long id, Model model) {
    // IDに基づいてデータを取得
    Customer customer = customerService.getCustomerById(id);
    
    // EntityをFormに変換
    CustomerForm customerForm = convertToForm(customer);
    
    model.addAttribute("customerForm", customerForm);
    model.addAttribute("pageTitle", "取引先編集");
    
    return "customers/edit";
}
```

#### 5. 編集処理（POST）

```java
@PostMapping("/edit/{id}")
public String edit(@PathVariable Long id,
                  @Valid @ModelAttribute CustomerForm customerForm,
                  BindingResult bindingResult,
                  Model model) {
    
    if (bindingResult.hasErrors()) {
        model.addAttribute("pageTitle", "取引先編集");
        return "customers/edit";
    }
    
    // IDを設定して更新
    customerForm.setId(id);
    customerService.updateCustomer(customerForm);
    
    return "redirect:/customers";
}
```

### テンプレートでのForm使用

#### 1. 一覧画面（検索フォーム）

```html
<!-- customers/list.html -->
<form th:action="@{/customers}" method="get" th:object="${searchForm}">
    <div class="card mb-6">
        <div class="flex flex-wrap gap-4">
            <div class="flex-1 min-w-64">
                <input type="text" 
                       th:field="*{name}" 
                       placeholder="取引先名で検索..." 
                       class="input-field">
            </div>
            <div class="flex-1 min-w-64">
                <input type="text" 
                       th:field="*{contactPerson}" 
                       placeholder="担当者名で検索..." 
                       class="input-field">
            </div>
            <select th:field="*{status}" class="input-field min-w-32">
                <option value="">すべて</option>
                <option value="active">アクティブ</option>
                <option value="inactive">非アクティブ</option>
            </select>
            <button type="submit" class="btn-secondary">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                </svg>
                検索
            </button>
        </div>
    </div>
</form>
```

#### 2. 作成・編集画面（入力フォーム）

```html
<!-- customers/create.html -->
<form th:action="@{/customers/create}" method="post" th:object="${customerForm}">
    <div class="card">
        <div class="space-y-6">
            <!-- 取引先名 -->
            <div>
                <label for="name" class="block text-sm font-medium text-gray-700 mb-2">
                    取引先名 <span class="text-red-500">*</span>
                </label>
                <input type="text" 
                       id="name"
                       th:field="*{name}" 
                       class="input-field"
                       th:classappend="${#fields.hasErrors('name')} ? 'border-red-500' : ''">
                <div th:if="${#fields.hasErrors('name')}" class="text-red-500 text-sm mt-1">
                    <span th:errors="*{name}"></span>
                </div>
            </div>
            
            <!-- 担当者名 -->
            <div>
                <label for="contactPerson" class="block text-sm font-medium text-gray-700 mb-2">
                    担当者名 <span class="text-red-500">*</span>
                </label>
                <input type="text" 
                       id="contactPerson"
                       th:field="*{contactPerson}" 
                       class="input-field"
                       th:classappend="${#fields.hasErrors('contactPerson')} ? 'border-red-500' : ''">
                <div th:if="${#fields.hasErrors('contactPerson')}" class="text-red-500 text-sm mt-1">
                    <span th:errors="*{contactPerson}"></span>
                </div>
            </div>
            
            <!-- 電話番号 -->
            <div>
                <label for="phone" class="block text-sm font-medium text-gray-700 mb-2">
                    電話番号 <span class="text-red-500">*</span>
                </label>
                <input type="tel" 
                       id="phone"
                       th:field="*{phone}" 
                       class="input-field"
                       th:classappend="${#fields.hasErrors('phone')} ? 'border-red-500' : ''">
                <div th:if="${#fields.hasErrors('phone')}" class="text-red-500 text-sm mt-1">
                    <span th:errors="*{phone}"></span>
                </div>
            </div>
            
            <!-- メールアドレス -->
            <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                    メールアドレス
                </label>
                <input type="email" 
                       id="email"
                       th:field="*{email}" 
                       class="input-field"
                       th:classappend="${#fields.hasErrors('email')} ? 'border-red-500' : ''">
                <div th:if="${#fields.hasErrors('email')}" class="text-red-500 text-sm mt-1">
                    <span th:errors="*{email}"></span>
                </div>
            </div>
            
            <!-- 備考 -->
            <div>
                <label for="notes" class="block text-sm font-medium text-gray-700 mb-2">
                    備考
                </label>
                <textarea id="notes"
                          th:field="*{notes}" 
                          rows="4"
                          class="input-field"
                          th:classappend="${#fields.hasErrors('notes')} ? 'border-red-500' : ''"></textarea>
                <div th:if="${#fields.hasErrors('notes')}" class="text-red-500 text-sm mt-1">
                    <span th:errors="*{notes}"></span>
                </div>
            </div>
            
            <!-- ボタン -->
            <div class="flex justify-end space-x-4">
                <a href="/customers" class="btn-secondary">キャンセル</a>
                <button type="submit" class="btn-primary">保存</button>
            </div>
        </div>
    </div>
</form>
```

### 新しい画面の追加手順

1. **ディレクトリ構造の作成**
   ```bash
   mkdir -p src/main/java/com/task/kanri/tools/tasks/SC_XX/{controller,service,repository,form}
   ```

2. **Formクラスの作成**
   ```java
   @Data
   public class YourForm {
       // フィールドとバリデーションアノテーション
   }
   ```

3. **コントローラーの作成**
   ```java
   @Controller
   @RequestMapping("/your-path")
   public class YourController {
       // GET/POSTメソッドの実装
   }
   ```

4. **テンプレートの作成**
   ```html
   <!DOCTYPE html>
   <html xmlns:th="http://www.thymeleaf.org" 
         xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout" 
         layout:decorate="~{layout/base}">
   ```

5. **Tailwind CSSの再ビルド**
   ```bash
   npm run build:css
   ```

### 命名規則

- **画面ID**: `SC_XX` (XXは連番)
- **パッケージ名**: 画面IDを小文字に変換
- **クラス名**: PascalCase
- **メソッド名**: camelCase
- **ファイル名**: クラス名と同じ
- **Formクラス**: `{機能名}Form`、`{機能名}SearchForm`

## 環境構築

### 前提条件

- Java 17以上
- Node.js 16以上
- Maven 3.6以上

### セットアップ手順

1. **依存関係のインストール**
   ```bash
   # Java依存関係
   mvn clean install
   
   # Node.js依存関係
   npm install
   ```

2. **Tailwind CSSのビルド**
   ```bash
   npm run build:css
   ```

3. **アプリケーションの起動**
   ```bash
   mvn spring-boot:run
   ```

4. **アクセス**
   - URL: `http://localhost:8080`
   - データベース: NEON DB (PostgreSQL)

## 開発時の注意事項

- 新しい画面を追加する際は、必ずSC_XX形式のディレクトリ構造に従う
- コントローラーは必ず対応する画面IDのディレクトリに配置する
- テンプレートは共通レイアウト（`layout/base.html`）を使用する
- Tailwind CSSのクラスを追加した場合は、必ず再ビルドする
- Formクラスには適切なバリデーションアノテーションを設定する
- エラーメッセージは日本語で分かりやすく記述する
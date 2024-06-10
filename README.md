# Payment API Documentation

Welcome to the documentation for the Payment API. This API provides endpoints for managing accounts, authentication, and file uploads related to accounts payable data.

## Swagger Documentation

You can access the Swagger documentation for the Payment API by navigating to the following URL:

[Swagger Documentation](http://localhost:8080/swagger-ui/index.html#/)

## Postman Collections

You can find the collections to import into Postman in the `collection` directory.

## Environment Configuration

### File Upload Directory

It is recommended to define the file upload directory using the `UPLOAD_DIR` environment variable. If not specified, the default directory `/media/urutau/fonts/payments/upload` will be used.

Ensure to set the `UPLOAD_DIR` variable in your environment configuration (.env file) before running the application.

## Account Endpoints

### Add a New Account

- **URL:** `/api/account/v1/`
- **Method:** `POST`
- **Description:** Adds a new account.
- **Request Body:** AccountDTO
- **Responses:**
    - 200 OK: Successful operation. Returns the created account.
    - 400 Bad Request: Invalid request format.
    - 401 Unauthorized: Authentication required.
    - 500 Internal Server Error: Server error.

### Find All Accounts

- **URL:** `/api/account/v1/`
- **Method:** `GET`
- **Description:** Finds all accounts.
- **Query Parameters:**
    - `page` (optional): Page number (default: 0).
    - `size` (optional): Page size (default: 12).
    - `direction` (optional): Sorting direction (asc or desc, default: asc).
- **Responses:**
    - 200 OK: Successful operation. Returns a paginated list of accounts.
    - 400 Bad Request: Invalid request format.
    - 401 Unauthorized: Authentication required.
    - 404 Not Found: No accounts found.
    - 500 Internal Server Error: Server error.

### Find Account by ID

- **URL:** `/api/account/v1/{id}`
- **Method:** `GET`
- **Description:** Finds an account by ID.
- **Path Parameters:**
    - `id`: ID of the account.
- **Responses:**
    - 200 OK: Successful operation. Returns the account.
    - 204 No Content: Account not found.
    - 400 Bad Request: Invalid request format.
    - 401 Unauthorized: Authentication required.
    - 404 Not Found: Account not found.
    - 500 Internal Server Error: Server error.

### Update an Account

- **URL:** `/api/account/v1/`
- **Method:** `PUT`
- **Description:** Updates an existing account.
- **Request Body:** AccountDTO
- **Responses:**
    - 200 OK: Account updated successfully. Returns the updated account.
    - 400 Bad Request: Invalid request format.
    - 401 Unauthorized: Authentication required.
    - 404 Not Found: Account not found.
    - 500 Internal Server Error: Server error.

### Update Account Status

- **URL:** `/api/account/v1/{id}/{status}`
- **Method:** `PUT`
- **Description:** Updates the status of an account.
- **Path Parameters:**
    - `id`: ID of the account.
    - `status`: New status of the account.
- **Responses:**
    - 400 Bad Request: Invalid request format.
    - 401 Unauthorized: Authentication required.
    - 404 Not Found: Account not found.
    - 500 Internal Server Error: Server error.

### Delete an Account

- **URL:** `/api/account/v1/{id}`
- **Method:** `DELETE`
- **Description:** Deletes an account.
- **Path Parameters:**
    - `id`: ID of the account.
- **Responses:**
    - 204 No Content: Account deleted successfully.
    - 400 Bad Request: Invalid request format.
    - 401 Unauthorized: Authentication required.
    - 404 Not Found: Account not found.
    - 500 Internal Server Error: Server error.

### Find Accounts Payable by Due Date and Description

- **URL:** `/api/account/v1/findByDueDateAndDescription`
- **Method:** `GET`
- **Description:** Finds accounts payable by due date and description.
- **Query Parameters:**
    - `page` (optional): Page number (default: 0).
    - `size` (optional): Page size (default: 12).
    - `direction` (optional): Sorting direction (asc or desc, default: asc).
- **Request Body:** AccountPayableDTO
- **Responses:**
    - 200 OK: Successful operation. Returns a paginated list of accounts payable.
    - 400 Bad Request: Invalid request format.
    - 401 Unauthorized: Authentication required.
    - 404 Not Found: No accounts payable found.
    - 500 Internal Server Error: Server error.

### Find Total Value by Period

- **URL:** `/api/account/v1/findTotalValueByPeriod`
- **Method:** `GET`
- **Description:** Finds total value by period.
- **Query Parameters:**
    - `page` (optional): Page number (default: 0).
    - `size` (optional): Page size (default: 12).
    - `direction` (optional): Sorting direction (asc or desc, default: asc).
- **Responses:**
    - 200 OK: Successful operation. Returns a paginated list of total values by period.
    - 400 Bad Request: Invalid request format.
    - 401 Unauthorized: Authentication required.
    - 404 Not Found: No total values found.
    - 500 Internal Server Error: Server error.

## Authentication Endpoints

### Sign In

- **URL:** `/auth/signin`
- **Method:** `POST`
- **Description:** Authenticates a user and returns a token.
- **Request Body:** AccountCredentialsVO
- **Responses:**
    - 200 OK: Successful authentication. Returns a token.
    - 401 Unauthorized: Invalid credentials.
    - 500 Internal Server Error: Server error.

### Refresh Token

- **URL:** `/auth/refresh/{username}`
- **Method:** `PUT`
- **Description:** Refreshes token for authenticated user and returns a new token.
- **Path Parameters:**
    - `username`: Username of the user.
- **Request Header:**
    - `Authorization`: Refresh token.
- **Responses:**
    - 200 OK: Token refreshed successfully. Returns a new token.
    - 401 Unauthorized: Invalid credentials.
    - 500 Internal Server Error: Server error.

## File Endpoints

### Upload a File

- **URL:** `/api/file/v1/uploadFile`
- **Method:** `POST`
- **Description:** Uploads a file containing accounts payable data.
- **Request Body:** MultipartFile
- **Responses:**
    - 200 OK: File uploaded successfully. Returns file details.
    - 400 Bad Request: Invalid request format.
    - 401 Unauthorized: Authentication required.
    - 500 Internal Server Error: Server error.

### Upload Multiple Files

- **URL:** `/api/file/v1/uploadMultipleFiles`
- **Method:** `POST`
- **Description:** Uploads multiple files containing accounts payable data.
- **Request Body:** Array of MultipartFile
- **Responses:**
    - 200 OK: Files uploaded successfully. Returns details of uploaded files.
    - 400 Bad Request: Invalid request format.
    - 401 Unauthorized: Authentication required.
    - 500 Internal Server Error: Server error.



./gradlew sonar \
-Dsonar.projectKey=payments \
-Dsonar.projectName='payments' \
-Dsonar.host.url=http://localhost:9000 \
-Dsonar.token=sqp_46d37f65925c77665813ba543c875288f894f954

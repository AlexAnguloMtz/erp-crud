import os

# Step 1: Read "API_URL" from environment variables
api_url = os.getenv('API_URL')

# Path to the environment file
file_path = '/app/src/environments/environment.ts'

# Step 2: Read the contents of the file into memory
with open(file_path, 'r') as file:
    file_content = file.read()

# Step 3: Replace occurrences of ${API_URL} with the actual API_URL value
updated_content = file_content.replace('${API_URL}', api_url)

# Step 4: Write the updated content back to the file
with open(file_path, 'w') as file:
    file.write(updated_content)

print("File updated successfully.")

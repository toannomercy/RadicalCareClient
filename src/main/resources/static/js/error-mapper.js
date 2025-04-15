/**
 * Error Mapper Utility for Registration Form
 * 
 * This utility maps backend API error messages to the expected format
 * for Selenium tests. It ensures consistent error message display.
 */

// Define known error mappings - keys are patterns to match in API responses
const errorMappings = {
    // Username errors
    'username.*required': 'Username is required',
    'username.*taken': 'Username already exists',
    'username.*exists': 'Username already exists',
    'username.*already': 'Username already exists',
    'username.*duplicate': 'Username already exists',
    'username.*length': 'Username must be between 1 and 50 characters',
    'username.*character': 'Username cannot contain special characters',
    'username.*invalid': 'Username cannot contain special characters',
    'username.*format': 'Username cannot contain special characters',
    
    // Email errors
    'email.*required': 'Email is required',
    'email.*taken': 'Email already exists',
    'email.*exists': 'Email already exists',
    'email.*already': 'Email already exists',
    'email.*duplicate': 'Email already exists',
    'email.*valid': 'Must be a valid email address',
    'email.*format': 'Must be a valid email address',
    'email.*invalid': 'Must be a valid email address',
    
    // Password errors
    'password.*required': 'Password is required',
    'password.*length': 'Password must be at least 3 characters',
    'password.*short': 'Password must be at least 3 characters',
    'password.*weak': 'Password must be at least 3 characters',
    
    // Phone errors
    'phone.*required': 'Phone is required',
    'phone.*digit': 'Phone must be number',
    'phone.*number': 'Phone must be number',
    'phone.*numeric': 'Phone must be number',
    'phone.*length': 'Phone must be 9-10 characters',
    'phone.*short': 'Phone must be 9-10 characters',
    'phone.*long': 'Phone must be 9-10 characters',
    
    // Address errors
    'address.*required': 'Address is required',
    
    // DOB errors
    'date.*required': 'Date of birth is required',
    'date.*format': 'Invalid date format',
    'date.*invalid': 'Invalid date format',
    'dob.*required': 'Date of birth is required',
    'dob.*format': 'Invalid date format',
    'dob.*invalid': 'Invalid date format',
    'birth.*required': 'Date of birth is required',
    'birth.*format': 'Invalid date format',
    'birth.*invalid': 'Invalid date format',
    
    // General errors for TC20
    'required': 'All fields are required',
    'fields.*required': 'All fields are required',
    'all.*required': 'All fields are required',
    
    // Common server errors
    'server error': 'Registration failed - Please try again later',
    'internal server': 'Server error - Please try again later',
    'service unavailable': 'Server unavailable - Please try again later',
    'bad request': 'Invalid form data',
    'conflict': 'Username or email already exists',
    '409': 'Username or email already exists',
    '400': 'Invalid form data',
    '500': 'Server error - Please try again later'
};

/**
 * Maps error messages from API responses to standardized format for tests
 * @param {string} errorMessage - The error message from the API
 * @returns {string} - The standardized error message
 */
function mapErrorMessage(errorMessage) {
    if (!errorMessage) return "An unknown error occurred";
    
    const lowerCaseError = errorMessage.toLowerCase();
    
    // Check each pattern for a match
    for (const [pattern, standardMessage] of Object.entries(errorMappings)) {
        const regex = new RegExp(pattern, 'i');
        if (regex.test(lowerCaseError)) {
            return standardMessage;
        }
    }
    
    // If no mapping found, return original message
    return errorMessage;
}

/**
 * Process an array of error messages for display
 * @param {Array} errors - Array of error messages
 * @returns {Array} - Array of standardized error messages
 */
function processErrors(errors) {
    if (!errors || !Array.isArray(errors)) return [];
    return errors.map(mapErrorMessage);
} 
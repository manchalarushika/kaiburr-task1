package com.rushika.task;

import org.springframework.stereotype.Component;

/**
 * Component responsible for validating shell commands to prevent malicious execution (security requirement).
 * It checks for common shell control characters and dangerous commands.
 */
@Component
public class CommandValidator {

    // Regex pattern matching common shell control characters and dangerous system commands.
    // The pattern is now correctly structured using explicit OR operators (|) and balanced parentheses.
    private static final String MALICIOUS_PATTERNS =
            // 1. Shell Control/Injection characters (&&, ;, |, `, $()), etc.
            "(.*(&&|;\\s*|\\|\\s*|`|\\$\\(|\\$@|\\$#|\\$\\*|\\$|\\>|\\<).*)"+
                    "|" + // OR operator to separate the command categories
                    // 2. Dangerous System Commands (case-insensitive and with potential whitespace)
                    "(.*(rm\\s|mv\\s|cp\\s|sudo|chown|chmod|wget|curl|nc|bash|sh|ssh|killall|reboot|shutdown|iptables|cat\\s/etc/passwd).*)" +
                    "|" + // OR operator to separate the command categories
                    // 3. Directory Traversal (../)
                    "(.*\\.\\./.*)";

    /**
     * Validates a shell command string.
     * @param command The command to validate.
     * @throws IllegalArgumentException if the command is considered unsafe or is empty.
     */
    public void validateCommand(String command) throws IllegalArgumentException {
        if (command == null || command.trim().isEmpty()) {
            throw new IllegalArgumentException("Command cannot be empty.");
        }

        String lowerCaseCommand = command.trim().toLowerCase();

        if (lowerCaseCommand.matches(MALICIOUS_PATTERNS)) {
            throw new IllegalArgumentException(
                    "Command contains potentially unsafe or malicious code (e.g., control characters or system commands) and has been rejected."
            );
        }
    }
}

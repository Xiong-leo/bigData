import random

# 定义最多 15 个不同的英文单词
words = [
    "apple", "banana", "cherry", "date", "elderberry",
    "fig", "grape", "honeydew", "kiwi", "lemon",
    "mango", "nectarine", "orange", "papaya", "quince"
]

# 生成 5000 个单词
total_words = 5000
generated_words = [random.choice(words) for _ in range(total_words)]

# 每行显示 10 个单词（可根据需要调整）
words_per_line = 10
for i in range(0, total_words, words_per_line):
    line = " ".join(generated_words[i:i + words_per_line])
    print(line)
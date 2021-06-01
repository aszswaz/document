# ffmpeg的简单使用

## 把pcm转换为mp3文件

```bash
$ ffmpeg -y -f s16be -ac 2 -ar 41000 -acodec pcm_s16le -i demo.pcm demo.mp3
```

<span style="color: green">41000是音频的码率， pcm_s16le是pcm解码器</span>


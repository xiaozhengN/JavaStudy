要实现将大视频文件切分成多个小视频文件的任务，您可以使用Python中的`moviepy`库来处理视频文件。以下是一个示例代码，满足您提到的细节要求：

```python
import os
from moviepy.editor import VideoFileClip

def split_video(input_file, output_dir, chunk_size):
    clip = VideoFileClip(input_file)
    duration = clip.duration
    num_chunks = int(duration / chunk_size) + 1

    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    for i in range(num_chunks):
        start_time = i * chunk_size
        end_time = min((i + 1) * chunk_size, duration)

        output_file = os.path.join(output_dir, f"chunk_{i+1}.mkv")
        subclip = clip.subclip(start_time, end_time)
        subclip.write_videofile(output_file)

    clip.close()

def main():
    input_file = r"E:\18-文件持久存储\01-OBS\CutterBefore\test_video.mkv"
    output_dir = r"E:\18-文件持久存储\01-OBS\CutterAfter"
    chunk_size = 3.9 * 60 * 60  # 3.9GB

    split_video(input_file, output_dir, chunk_size)

if __name__ == "__main__":
    main()
```

在上述代码中，我们定义了`split_video`函数来完成视频切分的操作。首先，我们使用`VideoFileClip`加载输入文件，然后根据指定的块大小和视频的总时长计算出要切分的块数。接下来，我们创建输出目录（如果不存在），然后循环遍历每个块，使用`subclip`方法从原始视频中提取指定时间范围的子剪辑，并将其写入到输出文件中。

最后，在`main`函数中，您可以设置输入文件路径、输出目录路径和块大小，并调用`split_video`函数来执行切分操作。运行整个程序时，视频文件将被切分成多个小视频文件，并存储在指定的输出目录中。

请注意，这可能需要一些时间来处理大型视频文件，具体取决于您的计算机性能和视频文件本身的大小。


-- --

这个错误通常表示无法找到指定的文件。在你的情况下，可能是`ffmpeg`可执行文件未正确配置或无法找到。

请尝试以下解决方法：

1. 确保已经正确安装了`ffmpeg`并将其添加到系统路径中。你可以通过在命令行运行`ffmpeg`来验证是否安装成功。

2. 如果已经安装了`ffmpeg`但仍然无法找到，请尝试手动指定`ffmpeg`的路径。

   ```python
   import os
   from pydub import AudioSegment

   # 设置ffmpeg路径
   ffmpeg_path = '/path/to/ffmpeg'
   AudioSegment.ffmpeg = ffmpeg_path

   # 继续导入pydub并使用它
   from pydub import AudioSegment
   ```

   请将`/path/to/ffmpeg`替换为你实际的`ffmpeg`路径。

3. 如果以上方法仍然不起作用，请检查`pydub`是否与所使用的操作系统兼容。某些库在特定的操作系统上可能会出现问题。

如果问题仍然存在，请提供更多关于你的操作系统、Python版本和其他相关信息，以便我能够更准确地帮助你解决问题。
[INFO][2023-10-04 03:47:54][wechat_mp_channel.py:77] - [WX_Public] reply content: 这个错误通常表示无法找到指定的文件。在你的情况下，可能是`ffmpeg`可执行文件未正确配置或无法找到。

请尝试以下解决方法：

1. 确保已经正确安装了`ffmpeg`并将其添加到系统路径中。你可以通过在命令行运行`ffmpeg`来验证是否安装成功。

2. 如果已经安装了`ffmpeg`但仍然无法找到，请尝试手动指定`ffmpeg`的路径。

   ```python
   import os
   from pydub import AudioSegment

   # 设置ffmpeg路径
   ffmpeg_path = '/path/to/ffmpeg'
   AudioSegment.ffmpeg = ffmpeg_path

   # 继续导入pydub并使用它
   from pydub import AudioSegment
   ```

   请将`/path/to/ffmpeg`替换为你实际的`ffmpeg`路径。

3. 如果以上方法仍然不起作用，请检查`pydub`是否与所使用的操作系统兼容。某些库在特定的操作系统上可能会出现问题。

如果问题仍然存在，请提供更多关于你的操作系统、Python版本和其他相关信息，以便我能够更准确地帮助你解决问题。


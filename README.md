# transformations
A Java 8 framework that provides:
  * A simple skeleton CLI
  * A framework for applying transformations to files and streams

Example usage:
```java
public class Example implements DataTransformation, Function<String, String> {

	private final String m_text;

	public Example(@Nonnull URL url) throws IOException {
		try (InputStream is = url.openStream()) {
			Path tmpFile = Files.createTempFile("dl", "tmp");
			Files.copy(is, tmpFile, StandardCopyOption.REPLACE_EXISTING);
			byte[] encoded = Files.readAllBytes(tmpFile);
			m_text = new String(encoded, StandardCharsets.UTF_8);
		}
	}

	public static void main(String[] args) throws Exception {
		new DataTransformationRunner(cli -> new Example(cli.getUrl("u").get()))
				.setClass(Example.class) // optional
				.setHelpHeader("This is an optional header")
				.setHelpFooter("This is an optional footer")
				.addOptions(Option.builder("u").longOpt("url").hasArgs().required().build())
				.run(args);
	}

	@Nonnull
	@Override
	public FileFilter getApplicableFiles() {
		return f -> f.getName().endsWith(".txt") || f.getName().endsWith(".txt.gz");
	}

	@Override
	public void apply(@Nonnull BufferedReader br, @Nonnull PrintWriter pw) throws IOException {
		br.lines().map(this).forEach(pw::println);
	}

	@Nonnull
	@Override
	public String apply(@Nonnull String s) {
		return s + m_text;
	}
}
```
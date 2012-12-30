import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

import org.jeelee.utils.Acceptable;

/* FileSizeClusterTest.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */

/**
 * <B>FileSizeClusterTest</B>
 * 
 * @author Brook Tran. Email: <a
 *         href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-23 created
 */
public class FileSizeClusterTest {

	class FileSizeClusterHelper {
		public static final long	KB				= 1024;
		public static final long	MB				= (KB * 1024);
		public static final long	GB				= (MB * 1024);
		public static final long	TB				= (GB * 1024);

		private static final int	MAX_DISTANCE	= 382;

		private FileSizeCounter[]	sizes;

		public FileSizeClusterHelper() {
			sizes = new FileSizeCounter[5120];
		}

		public void start() {
			List<FileCluster> clusters = new LinkedList<>();

			// init for the first cluster
			int index = 0;
			while (sizes[index++] == null) {
			}
			// for(index=0;index<sizes.length && sizes[index]!=null;index++) {}
			if (index > 0) {
				index--;
			}
			FileCluster currentCluster;
			currentCluster = addCluster(clusters, index);

			// the rest ones
			index++;
			for (; index < sizes.length; index++) {
				FileSizeCounter counter = sizes[index];

				if (counter == null) {
					continue;
				}

				if (index - currentCluster.getMaximum() < MAX_DISTANCE) {// if
																			// nears
																			// current
																			// cluster,
																			// then
																			// it
																			// should
																			// belong
																			// to
																			// it.
					currentCluster.add(sizes, index);
				} else {
					currentCluster = addCluster(clusters, index);
				}
			}

			// /XXX display result
			for (FileCluster fc : clusters) {
				int i = 0;
				System.out.println("cluster " + i + ":\t, num:" + fc.getCount()
						+ ", from " + fc.getMinima() + "\t-> "
						+ fc.getMaximum());
			}

		}

		private FileCluster addCluster(List<FileCluster> clusters, int index) {
			FileCluster currentCluster = new FileCluster(index, index);
			currentCluster.add(sizes, index);
			clusters.add(currentCluster);
			return currentCluster;
		}

		public void handleSize(Path file, BasicFileAttributes attrs) {
			// normalize the size to [0,5120]
			long size = attrs.size();
			int level = 0;

			while (size > 1024) {
				size = size >> 10;
				level++;
			}
			int normalSize = (int) (level * 1024 + size);

			if (sizes[normalSize] == null) {
				sizes[normalSize] = new FileSizeCounter();
			}
			sizes[normalSize].add(file);

		}
	}
}

class FileCluster implements Acceptable<Long> {
	private int		count;

	private long	minima;
	private long	maximum;

	public FileCluster(long minima, long maximum) {
		this.minima = minima;
		this.maximum = maximum;
	}

	public void add(FileSizeCounter[] sizes, int index) {
		setMaximum(index);
		count += sizes[index].getCount();
	}

	public void setMinima(long minima) {
		this.minima = minima;
	}

	public long getMinima() {
		return minima;
	}

	public void setMaximum(long maximum) {
		this.maximum = maximum;
	}

	public long getMaximum() {
		return maximum;
	}

	public int getCount() {
		return count;
	}

	@Override
	public boolean select(Long t) {
		return minima < t && maximum >= t;
	}

	public static final long		KB		= 1024;
	public static final long		MB		= (KB * 1024);
	public static final long		GB		= (MB * 1024);
	public static final long		TB		= (GB * 1024);

	public static final FileCluster	EMPTY	= new FileCluster(0, 0);
	public static final FileCluster	TINY	= new FileCluster(0, 10 * KB);
	public static final FileCluster	SMALL	= new FileCluster(10 * KB, 100 * KB);
	public static final FileCluster	MEDIUM	= new FileCluster(100 * KB, MB);
	public static final FileCluster	LARGE	= new FileCluster(1 * MB, 16 * MB);
	public static final FileCluster	HUGE	= new FileCluster(16 * MB, 128 * MB);

}
class FileSizeCounter {
	private List<Path> paths ;
	public FileSizeCounter(){
		paths = new LinkedList<Path>();
	}
	public void add(Path file) {
		paths.add(file);
	}
	public int getCount(){
		return paths.size();
	}
	
	
}
